package chill_logistics.hub_server.infrastructure.redis;

import java.time.Duration;

public record CacheHit <T>(T value, Duration ttlTime){}
