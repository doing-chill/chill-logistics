//package chill_logistics.firm_server.infrastructure.external.dto.response;
//
//import java.math.BigDecimal;
//
//public record FeignHubResponseV1 (
//    String name,
//    String hubManagerName,
//    String postalCode,
//    String country,
//    String region,
//    String city,
//    String district,
//    String roadName,
//    String buildingName,
//    String detailAddress,
//    String fullAddress,
//    BigDecimal latitude,
//    BigDecimal longitude
//){
//    public HubResponseV1 to() {
//        return new HubResponseV1(
//            this.name,
//            this.hubManagerName,
//            this.postalCode,
//            this.country,
//            this.region,
//            this.city,
//            this.district,
//            this.roadName,
//            this.buildingName,
//            this.detailAddress,
//            this.fullAddress,
//            this.latitude,
//            this.longitude
//        );
//    }
//}
