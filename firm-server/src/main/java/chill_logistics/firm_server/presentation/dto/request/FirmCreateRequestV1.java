package chill_logistics.firm_server.presentation.dto.request;

import chill_logistics.firm_server.application.dto.command.FirmCreateCommandV1;
import chill_logistics.firm_server.domain.entity.FirmType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record FirmCreateRequestV1(

    @Schema(description = "업체명", example = "강남 물류센터")
    @NotBlank(message = "FIRM_NAME_REQUIRED")
    String name,

    @Schema(description = "소속 허브 ID", example = "018f3d6c-9a12-7c13-b2a1-e3b893f00103")
    @NotNull(message = "HUB_ID_REQUIRED")
    UUID hubId,

    @Schema(description = "소유자 ID", example = "018f3d6c-aaaa-7bbb-cccc-111122223333")
    @NotNull(message = "OWNER_ID_REQUIRED")
    UUID ownerId,

    @Schema(description = "업체 유형 (CONSIGNOR / CONSIGNEE 등)", example = "CONSIGNOR")
    @NotNull(message = "FIRM_TYPE_REQUIRED")
    FirmType firmType,

    @Schema(description = "우편번호", example = "06123")
    @NotBlank(message = "POSTAL_CODE_REQUIRED")
    String postalCode,

    @Schema(description = "국가", example = "대한민국")
    @NotBlank(message = "COUNTRY_REQUIRED")
    String country,

    @Schema(description = "지역(도/광역시)", example = "서울특별시")
    @NotBlank(message = "REGION_REQUIRED")
    String region,

    @Schema(description = "시/군/구", example = "강남구")
    @NotBlank(message = "CITY_REQUIRED")
    String city,

    @Schema(description = "행정동/지번동", example = "역삼동", nullable = true)
    String district,

    @Schema(description = "도로명", example = "테헤란로 311")
    @NotBlank(message = "ROAD_NAME_REQUIRED")
    String roadName,

    @Schema(description = "건물명", example = "아남타워", nullable = true)
    String buildingName,

    @Schema(description = "상세주소", example = "6층 602호", nullable = true)
    String detailAddress,

    @Schema(description = "전체 주소", example = "서울특별시 강남구 테헤란로 311 아남타워 6층 602호")
    @NotBlank(message = "FULL_ADDRESS_REQUIRED")
    String fullAddress,

    @Schema(description = "위도", example = "37.5012743")
    @NotNull(message = "LATITUDE_REQUIRED")
    @Digits(integer = 3, fraction = 7, message = "LATITUDE_FORMAT_INVALID")
    BigDecimal latitude,

    @Schema(description = "경도", example = "127.0395850")
    @NotNull(message = "LONGITUDE_REQUIRED")
    @Digits(integer = 3, fraction = 7, message = "LONGITUDE_FORMAT_INVALID")
    BigDecimal longitude

){
    public FirmCreateCommandV1 to() {
        return new FirmCreateCommandV1(
            this.name,
            this.hubId,
            this.ownerId,
            //this.ownerName,
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
