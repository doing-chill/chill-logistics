package chill_logistics.firm_server.presentation.dto.request;

import chill_logistics.firm_server.application.dto.command.FirmUpdateCommandV1;
import chill_logistics.firm_server.domain.entity.FirmType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record FirmUpdateRequestV1 (
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
){
    public FirmUpdateCommandV1 to(){
        return new FirmUpdateCommandV1(
            this.name,
            this.hubId,
            this.ownerId,
            this.firmType,
            this.postalCode,
            this.country,
            this.region,
            this.city,
            this.district,
            this.roadName,
            this.buildingName,
            this.detailAddress,
            this.fullAddress,
            this.latitude,
            this.longitude
        );
    }
}
