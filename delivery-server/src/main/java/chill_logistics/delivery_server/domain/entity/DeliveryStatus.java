package chill_logistics.delivery_server.domain.entity;

import chill_logistics.delivery_server.presentation.ErrorCode;
import lib.web.error.BusinessException;

public enum DeliveryStatus {
    WAITING_FOR_HUB,            // 허브 이동 대기중
    MOVING_TO_HUB,              // 허브 이동중
    ARRIVED_DESTINATION_HUB,    // 목적지 허브 도착
    MOVING_TO_FIRM,             // 업체 이동중
    DELIVERY_IN_PROGRESS,       // 배송중
    DELIVERY_COMPLETED,         // 배송 완료
    DELIVERY_CANCELLED;         // 배송 취소

    // Kafka 메시지는 String deliveryStatus로 오므로 ENUM 변환 메서드 필요
    public static DeliveryStatus from(String status) {

        try {
            return DeliveryStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_DELIVERY_STATUS);
        }
    }

    public boolean canTransitTo(DeliveryStatus nextDeliveryStatus) {

        return switch (this) {
            case WAITING_FOR_HUB -> nextDeliveryStatus == MOVING_TO_HUB || nextDeliveryStatus == DELIVERY_CANCELLED;
            case MOVING_TO_HUB -> nextDeliveryStatus == ARRIVED_DESTINATION_HUB || nextDeliveryStatus == DELIVERY_CANCELLED;
            case ARRIVED_DESTINATION_HUB -> nextDeliveryStatus == MOVING_TO_FIRM || nextDeliveryStatus == DELIVERY_CANCELLED;
            case MOVING_TO_FIRM -> nextDeliveryStatus == DELIVERY_IN_PROGRESS || nextDeliveryStatus == DELIVERY_CANCELLED;
            case DELIVERY_IN_PROGRESS -> nextDeliveryStatus == DELIVERY_COMPLETED || nextDeliveryStatus == DELIVERY_CANCELLED;
            case DELIVERY_COMPLETED, DELIVERY_CANCELLED -> false;
        };
    }
}
