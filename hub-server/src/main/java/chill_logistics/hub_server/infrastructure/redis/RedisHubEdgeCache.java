package chill_logistics.hub_server.infrastructure.redis;

import chill_logistics.hub_server.application.vo.EdgeWeight;
import chill_logistics.hub_server.domain.entity.HubInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHubEdgeCache {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper om;

    public Optional<HubInfo> get(String key){
        String value = redisTemplate.opsForValue().get(key);
        if(value == null){
            return Optional.empty();
        }
        try {
            return Optional.of(om.readValue(value, HubInfo.class));
        } catch (Exception e) {
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

}
