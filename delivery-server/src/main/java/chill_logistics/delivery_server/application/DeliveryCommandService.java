package chill_logistics.delivery_server.application;

import chill_logistics.delivery_server.application.dto.command.HubRouteAfterCommandV1;
import chill_logistics.delivery_server.domain.entity.DeliveryStatus;
import chill_logistics.delivery_server.domain.entity.FirmDelivery;
import chill_logistics.delivery_server.domain.entity.HubDelivery;
import chill_logistics.delivery_server.domain.repository.FirmDeliveryRepository;
import chill_logistics.delivery_server.domain.repository.HubDeliveryRepository;
import chill_logistics.delivery_server.presentation.ErrorCode;
import chill_logistics.delivery_server.presentation.dto.request.DeliveryCancelRequestV1;
import chill_logistics.delivery_server.presentation.dto.request.DeliveryStatusChangeRequestV1;
import java.util.UUID;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryCommandService {

    private final HubDeliveryRepository hubDeliveryRepository;
    private final FirmDeliveryRepository firmDeliveryRepository;
    private final AsyncAiService asyncAiService;

    /* [허브 배송 생성 메서드]
     * Kafka 메시지로 order 정보 받아와서 허브 배송 생성
     */
    @Transactional
    public void createHubDelivery(HubRouteAfterCommandV1 message, UUID hubDeliveryPersonId) {

        log.info("[허브 배송 생성 시작] orderId={}", message.orderId());

        // 초기 배송 상태 & 배송 순서 셋팅
        DeliveryStatus deliveryStatus = DeliveryStatus.WAITING_FOR_HUB;
        // TODO: 배송순서 로직 수정 필요
        int deliverySequenceNum = 1;

        // HubDelivery 엔티티 생성
        HubDelivery hubDelivery = HubDelivery.createFrom(
            message,
            hubDeliveryPersonId,
            deliverySequenceNum,
            deliveryStatus
        );

        // 허브 배송 저장
        HubDelivery savedHubDelivery = hubDeliveryRepository.save(hubDelivery);

        log.info("[허브 배송 생성 완료] hubDeliveryId={}, orderId={}",
            savedHubDelivery.getId(), savedHubDelivery.getOrderId());
    }

    /* [업체 배송 생성 메서드]
     * Kafka 메시지로 order 정보 받아와서 업체 배송 생성
     */
    @Transactional
    public void createFirmDelivery(HubRouteAfterCommandV1 message, UUID firmDeliveryPersonId) {

        log.info("[업체 배송 생성 시작] orderId={}", message.orderId());

        // 초기 배송 상태 & 배송 순서 셋팅
        DeliveryStatus deliveryStatus = DeliveryStatus.MOVING_TO_FIRM;
        int deliverySequenceNum = 2;

        // FirmDelivery 엔티티 생성
        FirmDelivery firmDelivery = FirmDelivery.createFrom(
            message,
            firmDeliveryPersonId,
            deliverySequenceNum,
            deliveryStatus
        );

        // 업체 배송 저장
        FirmDelivery savedFirmDelivery = firmDeliveryRepository.save(firmDelivery);

        log.info("[업체 배송 생성 완료] firmDeliveryId={}, orderId={}",
            savedFirmDelivery.getId(), savedFirmDelivery.getOrderId());
    }

    /* [전체 배송 생성]
     * 허브 배송 + 업체 배송 = 전체 배송 생성
     * 전체 배송 생성 + AI 비동기 호출
     */
    @Transactional
    public void createDelivery(
        HubRouteAfterCommandV1 message,
        UUID hubDeliveryPersonId,
        UUID firmDeliveryPersonId) {

        log.info("[배송 생성 시작] orderId={}", message.orderId());

        createHubDelivery(message, hubDeliveryPersonId);
        createFirmDelivery(message, firmDeliveryPersonId);

        log.info("[배송 생성 완료] orderId={}", message.orderId());

        asyncAiService.sendDeadlineRequest(message);
    }

    /* [배송 상태 변경]
     * 허브 배송 / 업체 배송 상태 변경
     */
    @Transactional
    public void changeDeliveryStatus(UUID deliveryId, DeliveryStatusChangeRequestV1 request) {

        if (request.deliveryType() == DeliveryType.HUB) {
            HubDelivery hubDelivery = getHubDeliveryByIdOrThrow(deliveryId);

            hubDelivery.changeStatus(request.nextDeliveryStatus());
            log.info("[허브 배송 상태 변경] deliveryId={}, nextDeliveryStatus={}",
                deliveryId, request.nextDeliveryStatus());
        }

        if (request.deliveryType() == DeliveryType.FIRM) {
            FirmDelivery firmDelivery = getFirmDeliveryByIdOrThrow(deliveryId);

            firmDelivery.changeStatus(request.nextDeliveryStatus());
            log.info("[업체 배송 상태 변경] deliveryId={}, nextDeliveryStatus={}",
                deliveryId, request.nextDeliveryStatus());
        }
    }

    /* [배송 취소]
     * 허브 배송 / 업체 배송 취소
     */
    @Transactional
    public void cancelDelivery(UUID deliveryId, DeliveryCancelRequestV1 request) {

        if (request.deliveryType() == DeliveryType.HUB) {
            HubDelivery hubDelivery = getHubDeliveryByIdOrThrow(deliveryId);

            hubDelivery.cancelDelivery();

            log.info("[허브 배송 취소] deliveryId={}, orderId={}", deliveryId, hubDelivery.getOrderId());
        }

        if (request.deliveryType() == DeliveryType.FIRM) {
            FirmDelivery firmDelivery = getFirmDeliveryByIdOrThrow(deliveryId);

            firmDelivery.cancelDelivery();

            log.info("[업체 배송 취소] deliveryId={}, orderId={}", deliveryId, firmDelivery.getOrderId());
        }
    }

    private HubDelivery getHubDeliveryByIdOrThrow(UUID hubDeliveryId) {

        return hubDeliveryRepository.findById(hubDeliveryId)
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_DELIVERY_NOT_FOUND));
    }

    private FirmDelivery getFirmDeliveryByIdOrThrow(UUID firmDeliveryId) {

        return firmDeliveryRepository.findById(firmDeliveryId)
            .orElseThrow(() -> new BusinessException(ErrorCode.FIRM_DELIVERY_NOT_FOUND));
    }
}

/* TODO
 * deliveryPersonId 기반 deliveryPersonName 배정 필요
 * 배송 추적에 따라 상태 변경 로직 추가 필요
 * deliverySequenceNum 알고리즘에 따라 수정 필요
 * 전체 배송 생성 → @Async로 AsyncAiService 호출해서 데이터 전달
 */