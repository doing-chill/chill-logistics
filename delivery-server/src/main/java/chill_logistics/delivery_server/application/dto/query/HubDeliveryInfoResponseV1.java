package chill_logistics.delivery_server.application.dto.query;

import chill_logistics.delivery_server.domain.entity.DeliveryStatus;
import chill_logistics.delivery_server.domain.entity.HubDelivery;
import java.time.LocalDateTime;
import java.util.UUID;

public record HubDeliveryInfoResponseV1(
    UUID hubDeliveryId,
    UUID orderId,
    UUID startHubId,
    String startHubName,
    String startHubFullAddress,
    UUID endHubId,
    String endHubName,
    String endHubFullAddress,
    UUID deliveryPersonId,
    Integer deliverySequenceNum,
    DeliveryStatus deliveryStatus,
    Integer expectedDeliveryDuration,
    LocalDateTime createdAt,
    UUID createdBy,
    LocalDateTime updatedAt,
    UUID updatedBy,
    LocalDateTime deletedAt,
    UUID deletedBy) {

    public static HubDeliveryInfoResponseV1 from(HubDelivery hubDelivery) {

        return new HubDeliveryInfoResponseV1(
            hubDelivery.getId(),
            hubDelivery.getOrderId(),
            hubDelivery.getStartHubId(),
            hubDelivery.getStartHubName(),
            hubDelivery.getStartHubFullAddress(),
            hubDelivery.getEndHubId(),
            hubDelivery.getEndHubName(),
            hubDelivery.getEndHubFullAddress(),
            hubDelivery.getDeliveryPersonId(),
            hubDelivery.getDeliverySequenceNum(),
            hubDelivery.getDeliveryStatus(),
            hubDelivery.getExpectedDeliveryDuration(),
            hubDelivery.getCreatedAt(),
            hubDelivery.getCreatedBy(),
            hubDelivery.getUpdatedAt(),
            hubDelivery.getUpdatedBy(),
            hubDelivery.getDeletedAt(),
            hubDelivery.getDeletedBy()
        );
    }
}
