package chill_logistics.delivery_server.application;

import chill_logistics.delivery_server.domain.entity.DeliveryStatus;
import chill_logistics.delivery_server.domain.entity.FirmDelivery;
import chill_logistics.delivery_server.domain.entity.HubDelivery;
import chill_logistics.delivery_server.domain.repository.FirmDeliveryRepository;
import chill_logistics.delivery_server.domain.repository.HubDeliveryRepository;
import chill_logistics.delivery_server.infrastructure.client.HubClient;
import chill_logistics.delivery_server.infrastructure.client.dto.HubForDeliveryResponseV1;
import chill_logistics.delivery_server.infrastructure.kafka.dto.OrderAfterCreateV1;
import chill_logistics.delivery_server.presentation.ErrorCode;
import chill_logistics.delivery_server.presentation.dto.DeliveryCancelRequestV1;
import chill_logistics.delivery_server.presentation.dto.DeliveryStatusChangeRequestV1;
import java.util.UUID;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final HubDeliveryRepository hubDeliveryRepository;
    private final FirmDeliveryRepository firmDeliveryRepository;
    private final HubClient hubClient;

    /* [허브 배송 생성 메서드]
     * Kafka 메시지로 order 정보 + FeignClient로 hub 정보 받아와서 허브 배송 생성
     */
    @Transactional
    public void createHubDelivery(OrderAfterCreateV1 message, UUID hubDeliveryPersonId) {

        log.info("[허브 배송 생성 시작] orderId={}", message.orderId());

        // 1. Hub 서비스에서 허브 정보 조회 (Feign)
        HubForDeliveryResponseV1 startHub = hubClient.getHub(message.startHubId());
        HubForDeliveryResponseV1 endHub = hubClient.getHub(message.endHubId());

        // 2. 초기 배송 상태 & 배송 순서 셋팅
        DeliveryStatus deliveryStatus = DeliveryStatus.WAITING_FOR_HUB;
        // TODO: 배송순서 로직 수정 필요
        int deliverySequenceNum = 1;

        // 3. HubDelivery 엔티티 생성
        HubDelivery hubDelivery = HubDelivery.createFrom(
            message,
            startHub.hubName(),
            startHub.hubFullAddress(),
            endHub.hubName(),
            endHub.hubFullAddress(),
            hubDeliveryPersonId,
            deliverySequenceNum,
            deliveryStatus
        );

        // 4. 허브 배송 저장
        HubDelivery savedHubDelivery = hubDeliveryRepository.save(hubDelivery);

        log.info("[허브 배송 생성 완료] hubDeliveryId={}, orderId={}",
            savedHubDelivery.getId(), savedHubDelivery.getOrderId());
    }

    /* [업체 배송 생성 메서드]
     * Kafka 메시지로 order 정보 받아와서 업체 배송 생성
     */
    @Transactional
    public void createFirmDelivery(OrderAfterCreateV1 message, UUID firmDeliveryPersonId) {

        log.info("[업체 배송 생성 시작] orderId={}", message.orderId());

        // 1. 초기 배송 상태 & 배송 순서 셋팅
        DeliveryStatus deliveryStatus = DeliveryStatus.MOVING_TO_FIRM;
        int deliverySequenceNum = 2;

        // 2. FirmDelivery 엔티티 생성
        FirmDelivery firmDelivery = FirmDelivery.createFrom(
            message,
            firmDeliveryPersonId,
            deliverySequenceNum,
            deliveryStatus
        );

        // 3. 업체 배송 저장
        FirmDelivery savedFirmDelivery = firmDeliveryRepository.save(firmDelivery);

        log.info("[업체 배송 생성 완료] firmDeliveryId={}, orderId={}",
            savedFirmDelivery.getId(), savedFirmDelivery.getOrderId());
    }

    /* [전체 배송 생성]
     * 허브 배송 + 업체 배송 = 전체 배송 생성
     */
    @Transactional
    public void createDelivery(
        OrderAfterCreateV1 message,
        UUID hubDeliveryPersonId,
        UUID firmDeliveryPersonId) {

        log.info("[배송 생성 시작] orderId={}", message.orderId());

        createHubDelivery(message, hubDeliveryPersonId);
        createFirmDelivery(message, firmDeliveryPersonId);

        log.info("[배송 생성 완료] orderId={}", message.orderId());
    }

    /* [배송 상태 변경]
     * 허브 배송 / 업체 배송 상태 변경
     */
    @Transactional
    public void changeDeliveryStatus(UUID deliveryId, DeliveryStatusChangeRequestV1 request) {

        if (request.deliveryType() == DeliveryType.HUB) {
            HubDelivery hubDelivery = hubDeliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_DELIVERY_NOT_FOUND));

            hubDelivery.changeStatus(request.nextDeliveryStatus());
            log.info("[허브 배송 상태 변경] deliveryId={}, nextDeliveryStatus={}",
                deliveryId, request.nextDeliveryStatus());
        }

        if (request.deliveryType() == DeliveryType.FIRM) {
            FirmDelivery firmDelivery = firmDeliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FIRM_DELIVERY_NOT_FOUND));

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
            HubDelivery hubDelivery = hubDeliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_DELIVERY_NOT_FOUND));

            hubDelivery.cancelDelivery();

            log.info("[허브 배송 취소] deliveryId={}, orderId={}", deliveryId, hubDelivery.getOrderId());
        }

        if (request.deliveryType() == DeliveryType.FIRM) {
            FirmDelivery firmDelivery = firmDeliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FIRM_DELIVERY_NOT_FOUND));

            firmDelivery.cancelDelivery();

            log.info("[업체 배송 취소] deliveryId={}, orderId={}", deliveryId, firmDelivery.getOrderId());
        }
    }
}

/* TODO
 * deliveryPersonId 배정 로직 필요
 * 배송 추적에 따라 상태 변경 로직 추가 필요
 * deliverySequenceNum 알고리즘에 따라 수정 필요
 * 허브 배송 + 업체 배송 = 전체 배송 생성 (이 때, 나머지 데이터 필요: requestNote, productName, productQuantity, orderCreatedAt)
   → @Async로 AsyncAiService 호출해서 데이터 전달
 */