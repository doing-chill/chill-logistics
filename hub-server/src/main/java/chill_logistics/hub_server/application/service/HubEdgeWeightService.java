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
import chill_logistics.hub_server.infrastructure.redis.HubEdgeCacheKeys;
import chill_logistics.hub_server.infrastructure.redis.RedisHubEdgeCache;
import chill_logistics.hub_server.lib.error.ErrorCode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubEdgeWeightService implements HubEdgeWeightProvider {

    private final Duration redisTtl = Duration.ofSeconds(10);

    private final HubInfoRepository hubInfoRepository;
    private final HubRepository hubRepository;
    private final KakaoMapClient kakaoMapClient;
    private final RedisHubEdgeCache redisHubEdgeCache;
    private final CacheStats cacheStats;

    @Override
    @Transactional
    public EdgeWeight getWeight(UUID startHubId, UUID endHubId) {

        // 1) redis 조회 (15초인 상태)
        String keys = HubEdgeCacheKeys.edgeKeys(String.valueOf(startHubId), String.valueOf(endHubId));
        Optional<HubInfo> optionalHubInfo = redisHubEdgeCache.get(keys);
        if (optionalHubInfo.isPresent()) {
            cacheStats.hit();
            log.info("redis Cache hit");
            HubInfo hubInfo = optionalHubInfo.get();
            return new EdgeWeight(startHubId, endHubId, hubInfo.getDeliveryDuration(), hubInfo.getDistance());
        }
        cacheStats.miss();

        // 2) DB에 5분 이내의 캐시가 있으면 기존 값 그대로 사용
        HubInfo hubInfo = hubInfoRepository.findByStartHubIdAndEndHubId(startHubId, endHubId)
            .orElseThrow(()-> new BusinessException(ErrorCode.HUB_INFO_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();

        if (hubInfo.getDeliveryDuration() != null && hubInfo.getDistance() != null && !hubInfo.checkUpdateTime(now)) {

            redisHubEdgeCache.add(keys, hubInfo, redisTtl);
            log.info("[HubEdgeWeight] cache hit: {} -> {} ({}h {}m, {} km)",
                startHubId, endHubId, hubInfo.getDeliveryDuration()/3600, hubInfo.getDeliveryDuration() % 3600 / 60, hubInfo.getDistance());

            return new EdgeWeight(startHubId, endHubId, hubInfo.getDeliveryDuration(), hubInfo.getDistance());
        }

        // 3) DB에도 없다면 Kakao API로 실제 거리/시간 계산
        // 허브 존재 여부 확인
        Hub startHub = hubRepository.findById(hubInfo.getStartHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));
        Hub endHub = hubRepository.findById(hubInfo.getEndHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        // 해당 허브들에 위도, 경도값 추출
        String origin = startHub.getLongitude() + "," + startHub.getLatitude();
        String destination = endHub.getLongitude() + "," + endHub.getLatitude();

        DirectionInfoResponseV1 direction = kakaoMapClient.getDirection(
            origin,
            destination,
            2,          // carType
            "DIESEL",   // carFuel
            true        // carHipass
        );

        // Kakao 응답은 초 단위
        int durationSec = (int) direction.duration();
        BigDecimal distanceKm = BigDecimal
            .valueOf(direction.distance() / 1000.0)
            .setScale(3, RoundingMode.HALF_UP);

        hubInfo.updateDeliveryInfo(durationSec, distanceKm);
        // redis의 값은 after commit
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                redisHubEdgeCache.add(keys, hubInfo, redisTtl);
            }
        });

        log.info("[HubEdgeWeight] cache hit: {} -> {} ({}h {}m, {} km)",
            startHubId, endHubId, hubInfo.getDeliveryDuration() / 3600, hubInfo.getDeliveryDuration() % 3600 / 60, hubInfo.getDistance());

        return new EdgeWeight(startHubId, endHubId, durationSec, distanceKm);
    }
}
