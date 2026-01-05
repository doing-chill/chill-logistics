package chill_logistics.hub_server.infrastructure.redis;

public class HubEdgeCacheKeys {

    public static String edgeKeys(String fromHubId, String toHubId) {
        return "Hub:Edge" + fromHubId + ":" + toHubId;
    }
}
