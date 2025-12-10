package chill_logistics.firm_server.presentation.dto.response;

import chill_logistics.firm_server.application.dto.query.FirmInfoQueryV1;
import chill_logistics.firm_server.domain.entity.FirmType;
import java.math.BigDecimal;
import java.util.UUID;

public record FirmInfoResponseV1 (
    String name,
    UUID hubId,
    UUID ownerId,
    String ownerName,
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

){
    public static FirmInfoResponseV1 from(FirmInfoQueryV1 q) {
        return new FirmInfoResponseV1(
            q.name(),
            q.hubId(),
            q.ownerId(),
            q.ownerName(),
            q.firmType(),
            q.postalCode(),
            q.country(),
            q.region(),
            q.city(),
            q.district(),
            q.roadName(),
            q.buildingName(),
            q.detailAddress(),
            q.fullAddress(),
            q.latitude(),
            q.longitude()
        );
    }

}
