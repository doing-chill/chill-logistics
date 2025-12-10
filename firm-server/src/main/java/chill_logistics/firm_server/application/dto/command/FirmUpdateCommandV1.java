package chill_logistics.firm_server.application.dto.command;

import chill_logistics.firm_server.domain.entity.FirmType;
import java.math.BigDecimal;
import java.util.UUID;

public record FirmUpdateCommandV1 (
    String name,
    UUID hubId,
    UUID ownerId,
    FirmType firmType,
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