package chill_logistics.hub_server.presentation.dto.response;

import chill_logistics.hub_server.application.dto.query.HubRoadInfoQueryV1;
import java.math.BigDecimal;
import java.util.UUID;

public record HubRoadInfoResponseV1(

    UUID startHubId,
    String startHubName,
    UUID endHubId,
    String endHubName,
    Integer deliveryDuration,  // 분 단위
    BigDecimal distance             // km 단위

) {
    public static HubRoadInfoResponseV1 from (HubRoadInfoQueryV1 query) {
        return new HubRoadInfoResponseV1( query.startHubId(), query.startHubName(),
            query.endHubId(), query.endHubName(), query.deliveryDuration(), query.distance());
    }

}
