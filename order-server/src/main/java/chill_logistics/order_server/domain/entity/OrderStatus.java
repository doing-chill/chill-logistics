package chill_logistics.order_server.domain.entity;

public enum OrderStatus {
    CREATED,            // 주문 생성됨
    STOCK_PROCESSING,   // 재고 처리 중
    STOCK_CONFIRMED,    // 재고 확보 완료
    PROCESSING,         // 배송 준비
    IN_TRANSIT,         // 배송 중
    COMPLETED,          // 배송 완료
    CANCELED            // 주문 취소
}
