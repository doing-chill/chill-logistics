package chill_logistics.delivery_server.application.dto.query;

import chill_logistics.delivery_server.domain.entity.DeliveryStatus;
import chill_logistics.delivery_server.domain.entity.FirmDelivery;
import java.time.LocalDateTime;
import java.util.UUID;

public record FirmDeliveryInfoResponseV1(
    UUID firmDeliveryId,
    UUID orderId,
    UUID endHubId,
    UUID receiverFirmId,
    String receiverFirmFullAddress,
    String receiverFirmUserName,
    UUID deliveryPersonId,
    Integer deliverySequenceNum,
    DeliveryStatus deliveryStatus,
    LocalDateTime createdAt,
    UUID createdBy,
    LocalDateTime updatedAt,
    UUID updatedBy,
    LocalDateTime deletedAt,
    UUID deletedBy) {

    public static FirmDeliveryInfoResponseV1 from(FirmDelivery firmDelivery) {

        return new FirmDeliveryInfoResponseV1(
            firmDelivery.getId(),
            firmDelivery.getOrderId(),
            firmDelivery.getEndHubId(),
            firmDelivery.getReceiverFirmId(),
            firmDelivery.getReceiverFirmFullAddress(),
            firmDelivery.getReceiverFirmOwnerName(),
            firmDelivery.getDeliveryPersonId(),
            firmDelivery.getDeliverySequenceNum(),
            firmDelivery.getDeliveryStatus(),
            firmDelivery.getCreatedAt(),
            firmDelivery.getCreatedBy(),
            firmDelivery.getUpdatedAt(),
            firmDelivery.getUpdatedBy(),
            firmDelivery.getDeletedAt(),
            firmDelivery.getDeletedBy()
        );
    }
}
