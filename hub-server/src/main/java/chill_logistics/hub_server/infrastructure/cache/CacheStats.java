package chill_logistics.hub_server.infrastructure.cache;

import java.util.concurrent.atomic.LongAdder;
import org.springframework.stereotype.Component;

@Component
public class CacheStats {
    private final LongAdder hit = new LongAdder();
    private final LongAdder miss = new LongAdder();

    public void hit() { hit.increment(); }
    public void miss() { miss.increment(); }

    public long hits() { return hit.sum(); }
    public long misses() { return miss.sum(); }
}