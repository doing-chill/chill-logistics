package chill_logistics.hub_server.application.dto.query;

import chill_logistics.hub_server.domain.entity.HubInfo;
import java.math.BigDecimal;
import java.util.UUID;

public record HubRoadInfoQueryV1 (
    UUID startHubId,
    String startHubName,
    UUID endHubId,
    String endHubName,
    Integer deliveryDuration,  // 분 단위
    BigDecimal distance             // km 단위

){
    public static HubRoadInfoQueryV1 from(HubInfo hubInfo, String startHubName, String endHubName) {
        return new HubRoadInfoQueryV1(hubInfo.getStartHubId(), startHubName,
            hubInfo.getEndHubId(), endHubName, hubInfo.getDeliveryDuration(), hubInfo.getDistance());

    }
}
