package chill_logistics.delivery_server.presentation;

import chill_logistics.delivery_server.application.DeliveryService;
import chill_logistics.delivery_server.infrastructure.kafka.dto.OrderAfterCreateV1;
import chill_logistics.delivery_server.presentation.dto.DeliveryCreateRequestV1;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @param request 주문 정보가 담긴 Kafka 메시지 + 배송 담당자
     * @return status CREATED 반환
     */
    @PostMapping
    public BaseResponse<Void> createDelivery(@RequestBody DeliveryCreateRequestV1 request) {

        deliveryService.createDelivery(
            request.orderInfo(),
            request.hubDeliveryPersonId(),
            request.firmDeliveryPersonId()
        );

        return BaseResponse.ok(BaseStatus.CREATED);
    }
}
