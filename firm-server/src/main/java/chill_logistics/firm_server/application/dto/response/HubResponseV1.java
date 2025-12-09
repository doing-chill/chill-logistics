package chill_logistics.firm_server.application.dto.response;

import java.math.BigDecimal;

public record HubResponseV1(
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
){}
