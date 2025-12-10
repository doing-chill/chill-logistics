package chill_logistics.hub_server.application.dto.command;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CreateHubCommandV1 (
    String name,
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
    BigDecimal longitude,
    UUID userId
){}
