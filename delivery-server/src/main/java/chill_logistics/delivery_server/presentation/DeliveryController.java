package chill_logistics.delivery_server.presentation;

import chill_logistics.delivery_server.application.DeliveryCommandService;
import chill_logistics.delivery_server.application.DeliveryQueryService;
import chill_logistics.delivery_server.presentation.dto.request.DeliveryCancelRequestV1;
import chill_logistics.delivery_server.presentation.dto.request.DeliveryCreateRequestV1;
import chill_logistics.delivery_server.presentation.dto.request.DeliveryStatusChangeRequestV1;
import chill_logistics.delivery_server.presentation.dto.response.HubDeliveryPageResponseV1;
import java.util.UUID;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class DeliveryController {

    private final DeliveryCommandService deliveryCommandService;
    private final DeliveryQueryService deliveryQueryService;

    /**
     * [배송 생성]
     *
     * @param request 주문 정보가 담긴 Kafka 메시지 + 배송 담당자
     * @return status CREATED 반환
     */
    @PostMapping("/internal/create-delivery")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Void> createDelivery(@RequestBody DeliveryCreateRequestV1 request) {

        deliveryCommandService.createDelivery(
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
     * @param request    deliveryType(허브/업체), nextDeliveryStatus
     * @return status UPDATED 반환
     */
    @PatchMapping("/deliveries/{deliveryId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> changeDeliveryStatus(
        @PathVariable("deliveryId") UUID deliveryId,
        @RequestBody DeliveryStatusChangeRequestV1 request) {

        deliveryCommandService.changeDeliveryStatus(deliveryId, request);

        return BaseResponse.ok(BaseStatus.OK);
    }

    /**
     * [배송 취소]
     *
     * @param deliveryId 취소하려는 허브배송/업체배송의 UUID
     * @param request    deliveryType(허브/업체)
     * @return status DELETED 반환
     */
    @DeleteMapping("/deliveries/{deliveryId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> cancelDelivery(
        @PathVariable("deliveryId") UUID deliveryId,
        @RequestBody DeliveryCancelRequestV1 request) {

        deliveryCommandService.cancelDelivery(deliveryId, request);

        return BaseResponse.ok(BaseStatus.OK);
    }

    /**
     * [허브 배송 목록 조회]
     *
     * @param startHubName 허브 배송에서 검색하고자 하는 허브명
     * @param page 조회할 페이지 번호 (0부터 시작)
     * @param size 페이지 당 조회할 데이터 개수
     * @return 허브 배송 요약 정보 목록 + 페이징 정보
     */
    @GetMapping("/hub-deliveries")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<HubDeliveryPageResponseV1> searchHubDeliveries(
        @RequestParam(required = false) String startHubName,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {

        HubDeliveryPageResponseV1 response = deliveryQueryService.searchHubDeliveryByHubName(
            startHubName, page, size);

        return BaseResponse.ok(response, BaseStatus.OK);
    }
}
