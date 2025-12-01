package chill_logistics.delivery_server.domain.entity;

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
            throw new IllegalArgumentException("지원하지 않는 배송 상태입니다. status=" + status);
        }
    }
}
