package chill_logistics.hub_server.presentation.dto.request;

import chill_logistics.hub_server.application.dto.command.CreateHubCommandV1;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateHubRequestV1 (
    @NotBlank(message = "허브명은 필수입니다.")
    String name,

    String postalCode,

    @NotBlank(message = "국가는 필수입니다.")
    String country,

    @NotBlank(message = "지역(도/광역시)은 필수입니다.")
    String region,

    @NotBlank(message = "도시는 필수입니다.")
    String city,

    String district,

    @NotBlank(message = "도로명은 필수입니다.")
    String roadName,

    String buildingName,

    String detailAddress,

    @NotBlank(message = "전체 주소는 필수입니다.")
    String fullAddress,

    @NotNull(message = "위도는 필수입니다.")
    @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다.")
    @DecimalMax(value = "90.0", message = "위도는 90 이하이어야 합니다.")
    BigDecimal latitude,

    @NotNull(message = "경도는 필수입니다.")
    @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다.")
    @DecimalMax(value = "180.0", message = "경도는 180 이하이어야 합니다.")
    BigDecimal longitude
){

    public CreateHubCommandV1 toCreateHubCommand(CreateHubRequestV1 createHubRequest){
        return CreateHubCommandV1.builder()
            .name(this.name)
            .postalCode(this.postalCode)
            .country(this.country)
            .region(this.region)
            .city(this.city)
            .district(this.district)
            .roadName(this.roadName)
            .buildingName(this.buildingName)
            .detailAddress(this.detailAddress)
            .fullAddress(this.fullAddress)
            .latitude(this.latitude)
            .longitude(this.longitude).build();
    }
}
