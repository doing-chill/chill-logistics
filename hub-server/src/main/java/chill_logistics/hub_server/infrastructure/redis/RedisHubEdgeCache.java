package chill_logistics.hub_server.infrastructure.redis;

import chill_logistics.hub_server.application.vo.EdgeWeight;
import chill_logistics.hub_server.domain.entity.HubInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHubEdgeCache {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper om;

    public Optional<CacheHit<HubInfo>> get(String key){
        try {
            String value = redisTemplate.opsForValue().get(key);
            if(value == null) { return Optional.empty(); }

            long sec = redisTemplate.getExpire(key);
            Duration ttl = sec < 0 ? Duration.ZERO : Duration.ofSeconds(sec);

            return Optional.of(new CacheHit<>(om.readValue(value, HubInfo.class), ttl));

        } catch (Exception e) {
            // 역직렬화/redis 오류면 캐시 제거 후 miss 처리
            redisTemplate.delete(key);

            return Optional.empty();
        }
    }

    public void add(String key, HubInfo hubInfo, Duration ttl){
        try {
            redisTemplate.opsForValue().set(key, om.writeValueAsString(hubInfo), ttl);
        } catch (Exception ignored) {
            // 캐시 저장 실패는 복구 불가능 오류가 아니므로 실패를 시키지 않음
        }
    }

    // DB 조회/갱신은 1명만 가능하도록 Lock
    public boolean tryLock(String key, Duration ttl){
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "1", ttl);
        return Boolean.TRUE.equals(result);
    }

    public void unlock(String key){
        try { redisTemplate.delete(key); } catch (Exception ignored) {}
    }
}
