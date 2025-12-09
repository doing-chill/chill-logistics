package chill_logistics.hub_server.infrastructure.external.dto.response;


import chill_logistics.hub_server.application.dto.command.OrderAfterCommandV1;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderAfterCreateV1(
    UUID orderId,
    UUID supplierHubId,
    UUID receiverHubId,
    UUID receiverFirmId,
    String receiverFirmFullAddress,
    String receiverFirmOwnerName,
    String requestNote,
    String productName,
    int productQuantity,
    LocalDateTime orderCreatedAt) {

    // application 계층의 command 변환 메서드
    public OrderAfterCommandV1 toCommand() {
        return new OrderAfterCommandV1(
            this.orderId,
            this.supplierHubId,
            this.receiverHubId,
            this.receiverFirmId,
            this.receiverFirmFullAddress,
            this.receiverFirmOwnerName,
            this.requestNote,
            this.productName,
            this.productQuantity,
            this.orderCreatedAt
            );
    }
}