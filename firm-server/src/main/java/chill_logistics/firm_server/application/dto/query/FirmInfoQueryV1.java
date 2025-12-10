package chill_logistics.firm_server.application.dto.query;

import chill_logistics.firm_server.domain.entity.Firm;
import chill_logistics.firm_server.domain.entity.FirmType;
import java.math.BigDecimal;
import java.util.UUID;

public record FirmInfoQueryV1 (
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
    public static FirmInfoQueryV1 from(Firm firm){
        return new FirmInfoQueryV1(
            firm.getName(), firm.getHubId(), firm.getOwnerId(), firm.getOwnerName(), firm.getFirmType(),
            firm.getPostalCode(), firm.getCountry(), firm.getRegion(), firm.getCity(),
            firm.getDistrict(), firm.getRoadName(), firm.getBuildingName(), firm.getDetailAddress(),
            firm.getFullAddress(), firm.getLatitude(), firm.getLongitude()
        );
    }
}
