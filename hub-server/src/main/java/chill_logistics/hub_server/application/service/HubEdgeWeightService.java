package chill_logistics.hub_server.application.service;

import chill_logistics.hub_server.application.port.HubEdgeWeightProvider;
import chill_logistics.hub_server.application.port.KakaoMapClient;
import chill_logistics.hub_server.application.vo.EdgeWeight;
import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.entity.HubInfo;
import chill_logistics.hub_server.domain.repository.HubInfoRepository;
import chill_logistics.hub_server.domain.repository.HubRepository;
import chill_logistics.hub_server.infrastructure.actuator.CacheStats;
import chill_logistics.hub_server.infrastructure.external.dto.response.DirectionInfoResponseV1;
import chill_logistics.hub_server.infrastructure.redis.CacheHit;
import chill_logistics.hub_server.infrastructure.redis.HubEdgeCacheKeys;
import chill_logistics.hub_server.infrastructure.redis.RedisHubEdgeCache;
import chill_logistics.hub_server.lib.error.ErrorCode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubEdgeWeightService implements HubEdgeWeightProvider {

     private static final Duration REDIS_TTL = Duration.ofMinutes(6);

    // single-flight lock TTL (너무 길게 잡지 말고 “짧게”)
    private static final Duration LOCK_TTL = Duration.ofSeconds(10);

    // DB bulkhead: Redis 장애 시 DB 동시 접근 제한
    private final Semaphore dbBulkhead = new Semaphore(10);

    // jitter 재조회(50 -> 100 -> 200ms)
    private static final long[] JITTER_MS = {50, 150, 250};

    // Redis가 죽으면 Redis lock을 못 쓰니, JVM 내부 single-flight로 Kakao 폭주 방지 (같은 키에 대해 1명만 진입시키는 락 사용)
    private final ConcurrentHashMap<String, ReentrantLock> localLocks = new ConcurrentHashMap<>();

    private final HubInfoRepository hubInfoRepository;
    private final HubRepository hubRepository;
    private final KakaoMapClient kakaoMapClient;
    private final RedisHubEdgeCache redisHubEdgeCache;
    private final CacheStats cacheStats;
    private final TransactionTemplate transactionTemplate;

    @Override
    @Transactional
    public EdgeWeight getWeight(UUID startHubId, UUID endHubId) {

        String redisKey = HubEdgeCacheKeys.edgeKeys(String.valueOf(startHubId), String.valueOf(endHubId));
        final String refreshLockKey = redisKey + ":refreshLock";
        final String warmupLockKey = redisKey + ":warmupLock";
        boolean redisDown = false;

        LocalDateTime now = LocalDateTime.now();

        /** 1) Redis hit (항상 즉시 응답) */
        try {
            Optional<CacheHit<HubInfo>> redisHit = redisHubEdgeCache.get(redisKey);
            if (redisHit.isPresent()) {

                HubInfo hubInfo = redisHit.get().value();
                Duration ttlTime = redisHit.get().ttlTime();

                cacheStats.hit();
                log.info("redis Cache hit");

                // Redis TTL이 5분 이하면 값은 반환해서 주지만 Kakao API를 호출하여 시간 갱신
                if (ttlTime.compareTo(Duration.ofMinutes(5)) <= 0) {

                    // 동시에 refresh-ahead 트리거(1명만)
                    triggerRefreshAheadAsync(refreshLockKey, startHubId, endHubId, redisKey);
                }

                return new EdgeWeight(startHubId, endHubId, hubInfo.getDeliveryDuration(), hubInfo.getDistance());
            }
            cacheStats.miss();
        } catch (DataAccessException e) {

            // Redis 장애: 아래에서 DB fallback + bulkhead
            redisDown = true;
            log.warn("Redis down -> fallback to DB with bulkhead key={}", redisKey, e);
        }

        /** 2) Redis가 완전히 죽은 경우: DB로 "느리게라도 무조건" 반환 */
        if (redisDown) {
            return handleRedisDownMustReturnDb(startHubId, endHubId, redisKey, now);
        }

        /** 3) Redis miss (정상 Redis 동작) */
        return handleRedisMissNormal(startHubId, endHubId, warmupLockKey, refreshLockKey, redisKey, now);
    }

    /**
     * Redis가 완전히 죽었을 때 정책: - DB bulkhead는 "대기"해서라도 들어가는 방향으로 - DB에 시간/거리 값이 있으면 TTL 상관없이 무조건 반환
     * (단 리더는 무조건 최신값을 받게 구현 (DB TTL 만료 시 사용자가 한명만 접근하더라도 최신의 값을 주어야 되기 때문))

     * 예시 1 : Redis에 값이 없는 상황에서 DB에 값이 없는 상황 (cold start)
     * -> 로컬 single-flight로 1명만 Kakao Api 호출 -> bulkhead로 10명씩만 처리하기 때문에 많은 지연이 발생할 수 있음

     * 예시 2 : Redis가 없는 상황에서 DB에 값은 있지만 TTL이 만료된 상황 -> DB에서 값을 읽고 TTL이 만료된 값이라도 일단 반환 (bulkhead 적용) ->
     * 만료된 값을 받고 있다가 1명이 백그라운드 갱신 성공 시 최신의 값을 받게 됨 (여전히 bulkhead 적용)

     * - DB에 값이 없으면 cold-start처럼 외부 API 필요 (JVM single-flight로 Kakao api 폭주 방지)
     */
    private EdgeWeight handleRedisDownMustReturnDb(UUID startHubId, UUID endHubId, String redisKey, LocalDateTime now) {

        // 느리게라도 대기해서 bulkhead 진입
        dbBulkhead.acquireUninterruptibly();

        try {
            HubInfo hubInfo = hubInfoRepository.findByStartHubIdAndEndHubId(startHubId, endHubId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

            boolean hasValue = hubInfo.hasDeliveryInfo();

            // Redis 사망 + DB에 값도 없는 ColdStart 상황
            if (!hasValue) {
                return coldStartWithLocalSingleFlightSync(startHubId, endHubId, redisKey);
            }

            // DB에 값은 있지만 TTL이 만료 된 상황으로 우선 만료된 값을 주며 1명만 백그라운드 갱신
            if (hubInfo.checkUpdateTime(now)) {
                triggerRefreshWhenRedisDownAsync(startHubId, endHubId, redisKey);
            }

            return new EdgeWeight(startHubId, endHubId, hubInfo.getDeliveryDuration(), hubInfo.getDistance());
        } finally {
            dbBulkhead.release();
        }
    }

    /**
     * Redis down + DB TTL 만료(stale)여도 일단 반환하고, 1명만 백그라운드로 최신 갱신 시도
     * (중요: Thread에서 JPA 쓰려면 트랜잭션 필요 -> TransactionTemplate 사용)
     */
    private void triggerRefreshWhenRedisDownAsync(UUID startHubId, UUID endHubId, String redisKey) {

        ReentrantLock lock = localLocks.computeIfAbsent(redisKey, k -> new ReentrantLock());

        // reader가 아니라면 그냥 return
        if (!lock.tryLock()) {
            return;
        }

        new Thread(() -> {
            try {
                transactionTemplate.execute(status -> {
                HubInfo hubInfo = hubInfoRepository.findByStartHubIdAndEndHubId(startHubId, endHubId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

                // Redis는 죽었으니 DB만 갱신
                DirectionInfoResponseV1 directionInfoResponse = getKakaoMap(hubInfo);
                dbSet(directionInfoResponse.duration(), directionInfoResponse.distance(), hubInfo);

                // TransactionTemplate.execute()는 반드시 return 값이 필요하지만 지금은 필요가 없으므로 null 반환
                return null;
                });
            } catch (Exception e) {
                log.error("[RedisDownRefresh] failed {} -> {}", startHubId, endHubId, e);
            }finally {
                lock.unlock();
            }
        }).start();
    }

    /** Redis down + DB에도 값이 없는 cold-start
     * 리더 한명만 kakao Api를 호출하며 나머지는 DB 값이 채워질 때까지 짧게 재조회 시도
    */
    private EdgeWeight coldStartWithLocalSingleFlightSync(UUID startHubId, UUID endHubId, String redisKey) {

        ReentrantLock lock = localLocks.computeIfAbsent(redisKey, k -> new ReentrantLock());

        boolean tryLock = lock.tryLock();
        if (tryLock) {
            try {
                HubInfo hubInfo = hubInfoRepository.findByStartHubIdAndEndHubId(startHubId, endHubId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

                // 혹시 그 사이에 다른 트랜잭션에서 값이 채워졌으면 그대로 반환
                if (hubInfo.hasDeliveryInfo()){
                    return new EdgeWeight(startHubId, endHubId, hubInfo.getDeliveryDuration(), hubInfo.getDistance());
                }

                // DB만 갱신
                DirectionInfoResponseV1 directionInfoResponse = getKakaoMap(hubInfo);
                dbSet(directionInfoResponse.duration(), directionInfoResponse.distance(), hubInfo);

                return new EdgeWeight(startHubId, endHubId, hubInfo.getDeliveryDuration(), hubInfo.getDistance());
            }finally {
                lock.unlock();
            }
        }

        for(long ms : JITTER_MS){
            sleep(ms);
            HubInfo hubInfo = hubInfoRepository.findByStartHubIdAndEndHubId(startHubId, endHubId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

            // 값이 DB에 갱신이 되었으면 반환
            if (hubInfo.hasDeliveryInfo()){
                return new EdgeWeight(startHubId, endHubId, hubInfo.getDeliveryDuration(), hubInfo.getDistance());
            }
        }
        throw new BusinessException(ErrorCode.TEMPORARILY_UNAVAILABLE);
    }

    /**
     * Redis 서버는 정상인데 TTL 만료인 경우: warmupLeader 1명만 DB 내려가서 Redis 채움 -> 나머지는 jitter로 Redis 재조회
     * DB에 값도 TTL 만료 상태라면 refreshLock으로 1명만 Kakao 호출 (리더는 최신값으로 응답하도록 구성)
     * 예시 (Redis 만료 + DB도 TTL 만료 상황에서 200명 접근 시 리더 1명만 최신 값을 받고 나머지 199명은 redis에 재처리 3회를 하면서 리더가 갱신한 값을 받게 됨)
     * 3회 처리에도 받지 못한다면 에러를 띄움
     */
    private EdgeWeight handleRedisMissNormal(UUID startHubId, UUID endHubId, String warmupLockKey,
        String refreshLockKey, String redisKey, LocalDateTime now) {

        // DB에 값이 있다면 한명만 갱신 수행
        boolean warmupLeader = redisHubEdgeCache.tryLock(warmupLockKey, LOCK_TTL);

        if (warmupLeader) {
            try {
                HubInfo hubInfo = hubInfoRepository.findByStartHubIdAndEndHubId(startHubId, endHubId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

                //DB에도 값이 없으면 cold-start (1명만 Kakao 동기 호출해서 생성 후 응답)
                if (! hubInfo.hasDeliveryInfo()) {
                    return coldStartFetchWithRedisLock(startHubId, endHubId, refreshLockKey, redisKey, hubInfo);
                }
                // DB의 값이 존재하고 TTL도 살아 있으면 Redis에 warm-up 후 즉시 응답
                if (!hubInfo.checkUpdateTime(now)) { // false = fresh
                    redisHubEdgeCache.add(redisKey, hubInfo, REDIS_TTL);
                    return new EdgeWeight(startHubId, endHubId, hubInfo.getDeliveryDuration(), hubInfo.getDistance());
                }

                // Redis 서버는 살아 있고 DB에 값도 있지만 DB 값의 TTL이 만료 된 상황이라면 예전 값이라도 주는 동시에 1명은 값 갱신
                boolean leader = redisHubEdgeCache.tryLock(refreshLockKey, LOCK_TTL);
                if (leader) {
                    try {
                        // 리더는 항상 최신값을 받아서 응답
                        DirectionInfoResponseV1 directionInfoResponse = getKakaoMap(hubInfo);
                        RedisAndDbSet(directionInfoResponse.duration(), directionInfoResponse.distance(), hubInfo, redisKey);
                        return new EdgeWeight(startHubId, endHubId, hubInfo.getDeliveryDuration(), hubInfo.getDistance());
                    } finally {
                        redisHubEdgeCache.unlock(refreshLockKey);
                    }
                }
                // warmupLeader가 되지 못한 나머지 요청들은 우선 db에 값을 응답받고 jitter로 Redis에 값 생성을 기다리게 된다
            } finally {
                redisHubEdgeCache.unlock(warmupLockKey);
            }
        }

        return jitterWaitRedis(startHubId, endHubId, redisKey)
            .orElseThrow(() -> new BusinessException(ErrorCode.TEMPORARILY_UNAVAILABLE));
    }

    private EdgeWeight coldStartFetchWithRedisLock(UUID startHubId, UUID endHubId, String refreshLockKey, String redisKey, HubInfo hubInfo) {
        boolean leader = redisHubEdgeCache.tryLock(refreshLockKey, LOCK_TTL);

        if (leader) {
            try {
                DirectionInfoResponseV1 directionInfoResponse = getKakaoMap(hubInfo);
                RedisAndDbSet(directionInfoResponse.duration(), directionInfoResponse.distance(), hubInfo, redisKey);

                // 리더는 최신값으로 즉시 응답
                return new EdgeWeight(startHubId, endHubId, hubInfo.getDeliveryDuration(), hubInfo.getDistance());
            } finally {
                redisHubEdgeCache.unlock(refreshLockKey);
            }
        }

        return jitterWaitRedis(startHubId, endHubId, redisKey)
            .orElseThrow(() -> new BusinessException(ErrorCode.TEMPORARILY_UNAVAILABLE));
    }

    /**
     * Redis hit TTL 임박 시 한명만 값 갱신
     */
    private void triggerRefreshAheadAsync(String refreshLockKey, UUID startHubId, UUID endHubId, String redisKey) {
        boolean leader = redisHubEdgeCache.tryLock(refreshLockKey, LOCK_TTL);

        // 리더로 선정되지 못하면 반환
        if (!leader)
            return;

        // 선정된 리더만 비동기로 외부 API(Kakao Map)를 호출해 Redis + DB 갱신
        new Thread(() -> {
            try {
                transactionTemplate.execute(status -> {
                HubInfo hubInfo = hubInfoRepository.findByStartHubIdAndEndHubId(startHubId, endHubId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

                // Kakao api 호출
                DirectionInfoResponseV1 directionInfoResponse = getKakaoMap(hubInfo);
                // 결과를 Redis와 DB에 갱신
                RedisAndDbSet(directionInfoResponse.duration(), directionInfoResponse.distance(), hubInfo, redisKey);
                return null;
                });
            } catch (Exception e) {
                log.error("[RefreshAhead] failed {} -> {}", startHubId, endHubId, e);
            } finally {
                redisHubEdgeCache.unlock(refreshLockKey); // 실패해도 락은 반드시 해제
            }
        }).start();
    }

    private DirectionInfoResponseV1 getKakaoMap(HubInfo hubInfo) {

        // 허브 존재 여부 확인
        Hub startHub = hubRepository.findById(hubInfo.getStartHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));
        Hub endHub = hubRepository.findById(hubInfo.getEndHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        // 해당 허브들에 위도, 경도값 추출
        String origin = startHub.getLongitude() + "," + startHub.getLatitude();
        String destination = endHub.getLongitude() + "," + endHub.getLatitude();

        // Kakao Api 호출
        return kakaoMapClient.getDirection(
            origin,
            destination,
            2,          // carType
            "DIESEL",          // carFuel
            true               // carHipass
        );
    }

    private void RedisAndDbSet(long duration, long distance, HubInfo hubInfo, String redisKey) {
        // Kakao 응답은 초 단위
        int durationSec = (int) duration;

        BigDecimal distanceKm = BigDecimal
            .valueOf(distance / 1000.0)
            .setScale(3, RoundingMode.HALF_UP);

        hubInfo.updateDeliveryInfo(durationSec, distanceKm);

        // redis의 값은 after commit
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                redisHubEdgeCache.add(redisKey, hubInfo, REDIS_TTL);
            }
        });
    }

    private void dbSet(long duration, long distance, HubInfo hubInfo) {
        // Kakao 응답은 초 단위
        int durationSec = (int) duration;

        BigDecimal distanceKm = BigDecimal
            .valueOf(distance / 1000.0)
            .setScale(3, RoundingMode.HALF_UP);

        hubInfo.updateDeliveryInfo(durationSec, distanceKm);
    }

    /** reader로 선출되지 못하고 Redis에 값 갱신을 기다리는 애들을 위한 jitter 재조회 */
    private Optional<EdgeWeight> jitterWaitRedis(UUID startHubId, UUID endHubId, String redisKey){
        for (long ms : JITTER_MS) {
            sleep(ms);

            try {
                Optional<CacheHit<HubInfo>> hubInfoCacheHit = redisHubEdgeCache.get(redisKey);
                if (hubInfoCacheHit.isPresent()) {
                    HubInfo hubInfo = hubInfoCacheHit.get().value();
                    return Optional.of(new EdgeWeight(startHubId, endHubId, hubInfo.getDeliveryDuration(), hubInfo.getDistance()));
                }
            } catch (DataAccessException e) {
                return Optional.empty();
            }
        }
            return Optional.empty();
    }

    /** 이 스레드를 잠깐 재우지만 다른 스레드가 interrupt(중단 신호)로 깨우면 신호를 받고 중지
     (대기중 상황에서 타임아웃 발생 & 클라이언트가 연결을 끊음 & 서버 종료 등) */
    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}