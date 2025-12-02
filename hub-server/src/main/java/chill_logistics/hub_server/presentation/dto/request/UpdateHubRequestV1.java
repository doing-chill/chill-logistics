package chill_logistics.hub_server.presentation.dto.request;

import chill_logistics.hub_server.application.dto.command.UpdateHubCommandV1;
import java.math.BigDecimal;
import java.util.UUID;

public record UpdateHubRequestV1 (
    String name,
    UUID hubManagerId,
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
    public UpdateHubCommandV1 toUpdateHubCommandV1() {
        return new UpdateHubCommandV1(
            this.name,
            this.hubManagerId,
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
            this.longitude);
    }

}
