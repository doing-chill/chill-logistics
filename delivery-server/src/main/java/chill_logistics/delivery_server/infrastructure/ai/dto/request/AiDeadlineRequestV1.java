package chill_logistics.delivery_server.infrastructure.ai.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AiDeadlineRequestV1(
    UUID orderId,                   // 주문 번호
    String receiverFirmOwnerName,   // 주문자 정보
    LocalDateTime orderCreatedAt,   // 주문 시각
    String productName,             // 상품명
    int productQuantity,            // 상품 수량
    String requestNote,             // 요청 사항
    String supplierHubName,         // 발송지
    List<String> viaHubsNames,      // 경유지 정보 ("대전 센터, 부산 센터" or "업음")
    String receiverFirmFullAddress, // 도착지 전체 주소
    String deliveryPersonName,      // 배송 담당자 이름
    int expectedDeliveryDuration    // 예상 총 소요 시간
) {}
