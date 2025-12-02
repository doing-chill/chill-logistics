package chill_logistics.hub_server.presentation.dto.response;

import chill_logistics.hub_server.application.dto.query.HubInfoQueryV1;
import java.math.BigDecimal;

public record HubInfoResponseV1 (
    String name,
    String hubManagerName,
    String postalCode,
    String country,
    String region,
    String city,
    String district,
    String roadName,
    String buildingName,
    String detailAddress,
    String fullAddress,
    BigDecimal latitude,
    BigDecimal longitude
){
    public static HubInfoResponseV1 from (HubInfoQueryV1 hubInfoQuery) {

        return new HubInfoResponseV1(hubInfoQuery.name(), hubInfoQuery.hubManagerName(), hubInfoQuery.postalCode(),
            hubInfoQuery.country(), hubInfoQuery.region(), hubInfoQuery.city(), hubInfoQuery.district(),
            hubInfoQuery.roadName(), hubInfoQuery.buildingName(), hubInfoQuery.detailAddress(), hubInfoQuery.fullAddress(),
            hubInfoQuery.latitude(), hubInfoQuery.longitude());
    }

}
