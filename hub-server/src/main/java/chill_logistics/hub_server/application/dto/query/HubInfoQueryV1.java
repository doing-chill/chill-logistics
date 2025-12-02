package chill_logistics.hub_server.application.dto.query;

import chill_logistics.hub_server.domain.entity.Hub;
import java.math.BigDecimal;
import java.util.UUID;

public record HubInfoQueryV1(
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
    public static HubInfoQueryV1 fromHub(Hub hub, String hubManagerName) {
        return new HubInfoQueryV1(hub.getName(), hubManagerName, hub.getPostalCode(),
            hub.getCountry(), hub.getRegion(), hub.getCity(), hub.getDistrict(),
            hub.getRoadName(), hub.getBuildingName(), hub.getDetailAddress(), hub.getFullAddress(),
            hub.getLatitude(), hub.getLongitude());

    }

}
