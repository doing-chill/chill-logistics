package chill_logistics.hub_server.infrastructure.cache;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "cache-stats")
@RequiredArgsConstructor
public class CacheStatsEndPointConfig {

    private final CacheStats cacheStats;

    @ReadOperation
    public Map<String, Object> stats() {
        long hit = cacheStats.hits();
        long miss = cacheStats.misses();
        long total = hit+miss;

        double hitRate = (total == 0) ? 0.0 : (hit * 100.0 / total);

        return Map.of("hits", hit, "misses", miss, "total", total, "hitRate", hitRate);
    }
}
