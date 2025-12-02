package chill_logistics.delivery_server.presentation;

import chill_logistics.delivery_server.application.DeliveryService;
import chill_logistics.delivery_server.presentation.dto.DeliveryCreateRequestV1;
import chill_logistics.delivery_server.presentation.dto.DeliveryStatusChangeRequestV1;
import java.util.UUID;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class DeliveryController {

    private final DeliveryService deliveryService;

    /**
     * [배송 생성]
     *
     * @param request 주문 정보가 담긴 Kafka 메시지 + 배송 담당자
     * @return status CREATED 반환
     */
    @PostMapping("/internal/create-delivery")
    public BaseResponse<Void> createDelivery(@RequestBody DeliveryCreateRequestV1 request) {

        deliveryService.createDelivery(
            request.orderInfo(),
            request.hubDeliveryPersonId(),
            request.firmDeliveryPersonId()
        );

        return BaseResponse.ok(BaseStatus.CREATED);
    }

    /**
     * [배송 상태 변경]
     *
     * @param deliveryId 배송 상태를 변경하려는 허브배송/업체배송의 UUID
     * @param request deliveryType(허브/업체), nextDeliveryStatus
     * @return status UPDATED 반환
     */
    @PatchMapping("/deliveries/{deliveryId}")
    public BaseResponse<Void> changeDeliveryStatus(
        @PathVariable("deliveryId") UUID deliveryId,
        @RequestBody DeliveryStatusChangeRequestV1 request) {

        deliveryService.changeDeliveryStatus(deliveryId, request);

        return BaseResponse.ok(BaseStatus.UPDATED);
    }
}
