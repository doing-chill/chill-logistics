package chill_logistics.delivery_server.presentation;

import chill_logistics.delivery_server.application.DeliveryService;
import chill_logistics.delivery_server.infrastructure.kafka.dto.OrderAfterCreateV1;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    /**
     * [배송 생성]
     *
     * @param message 주문 정보가 담긴 Kafka 메시지
     * @return status CREATED 반환
     */
    @PostMapping
    public BaseResponse<Void> createDelivery(OrderAfterCreateV1 message) {

        deliveryService.createDelivery(message);

        return BaseResponse.ok(BaseStatus.CREATED);
    }
}
