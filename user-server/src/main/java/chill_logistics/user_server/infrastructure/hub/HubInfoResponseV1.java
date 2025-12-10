package chill_logistics.user_server.infrastructure.hub;

import java.math.BigDecimal;

public record HubInfoResponseV1(
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
) { }
