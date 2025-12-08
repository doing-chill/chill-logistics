package chill_logistics.delivery_server.application;

import chill_logistics.delivery_server.application.dto.command.AssignedDeliveryPersonV1;
import chill_logistics.delivery_server.application.dto.command.HubRouteAfterCommandV1;
import chill_logistics.delivery_server.domain.entity.DeliveryStatus;
import chill_logistics.delivery_server.domain.entity.FirmDelivery;
import chill_logistics.delivery_server.domain.entity.HubDelivery;
import chill_logistics.delivery_server.domain.repository.FirmDeliveryRepository;
import chill_logistics.delivery_server.domain.repository.HubDeliveryRepository;
import chill_logistics.delivery_server.presentation.ErrorCode;
import chill_logistics.delivery_server.presentation.dto.request.DeliveryCancelRequestV1;
import chill_logistics.delivery_server.presentation.dto.request.DeliveryStatusChangeRequestV1;
import java.util.List;
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
    private final DeliveryPersonAssignmentService deliveryPersonAssignmentService;

    /* [허브 배송 생성 메서드]
     * Kafka 메시지로 order 정보 받아와서 허브 배송 생성
     */
    @Transactional
    public void createHubDelivery(
        HubRouteAfterCommandV1 message,
        UUID hubDeliveryPersonId,
        int deliverySequenceNum) {

        log.info("[허브 배송 생성 시작] orderId={}, deliverySequenceNum={}",
            message.orderId(), deliverySequenceNum);

        // 초기 배송 상태 셋팅
        DeliveryStatus deliveryStatus = DeliveryStatus.WAITING_FOR_HUB;

        // HubDelivery 엔티티 생성
        HubDelivery hubDelivery = HubDelivery.createFrom(
            message,
            hubDeliveryPersonId,
            deliverySequenceNum,
            deliveryStatus
        );

        // 허브 배송 저장
        HubDelivery savedHubDelivery = hubDeliveryRepository.save(hubDelivery);

        log.info("[허브 배송 생성 완료] hubDeliveryId={}, orderId={}, deliverySequenceNum={}",
            savedHubDelivery.getId(),
            savedHubDelivery.getOrderId(),
            savedHubDelivery.getDeliverySequenceNum());
    }

    /* [업체 배송 생성 메서드]
     * Kafka 메시지로 order 정보 받아와서 업체 배송 생성
     */
    @Transactional
    public void createFirmDelivery(
        HubRouteAfterCommandV1 message,
        UUID firmDeliveryPersonId,
        int deliverySequenceNum) {

        log.info("[업체 배송 생성 시작] orderId={}, deliverySequenceNum={}",
            message.orderId(), deliverySequenceNum);

        // 초기 배송 상태 셋팅
        DeliveryStatus deliveryStatus = DeliveryStatus.MOVING_TO_FIRM;

        // FirmDelivery 엔티티 생성
        FirmDelivery firmDelivery = FirmDelivery.createFrom(
            message,
            firmDeliveryPersonId,
            deliverySequenceNum,
            deliveryStatus
        );

        // 업체 배송 저장
        FirmDelivery savedFirmDelivery = firmDeliveryRepository.save(firmDelivery);

        log.info("[업체 배송 생성 완료] firmDeliveryId={}, orderId={}, deliverySequenceNum={}",
            savedFirmDelivery.getId(),
            savedFirmDelivery.getOrderId(),
            savedFirmDelivery.getDeliverySequenceNum());
    }

    /* [전체 배송 생성]
     * 허브 배송 + 업체 배송 = 전체 배송 생성
     * 전체 배송 생성 + AI + Discord 비동기 호출
     */
    @Transactional
    public void createDelivery(HubRouteAfterCommandV1 message) {

        log.info("[배송 생성 시작] orderId={}", message.orderId());

        // 허브 배송 담당자 배정
        AssignedDeliveryPersonV1 hubDeliveryPerson =
            deliveryPersonAssignmentService.assignHubDeliveryPerson();

        // 업체 배송 담당자 배정
        AssignedDeliveryPersonV1 firmDeliveryPerson =
            deliveryPersonAssignmentService.assignFirmDeliveryPerson();

        UUID hubDeliveryPersonId = hubDeliveryPerson.userId();
        String hubDeliveryPersonName = hubDeliveryPerson.userName();
        UUID firmDeliveryPersonId = firmDeliveryPerson.userId();

        // pathHubIds 기반 deliverySequenceNum 계산
        int hubDeliverySequenceNum = calculateHubSequenceNum(message.pathHubIds());
        int firmDeliverySequenceNum = hubDeliverySequenceNum + 1;

        createHubDelivery(message, hubDeliveryPersonId, hubDeliverySequenceNum);
        createFirmDelivery(message, firmDeliveryPersonId, firmDeliverySequenceNum);

        log.info(
            "[배송 생성 완료 & 배송 담당자 배정 완료] orderId={}, hubDeliveryPersonId={}, firmDeliveryPersonId={}",
            message.orderId(), hubDeliveryPersonId, firmDeliveryPersonId);

        // AI + Discord 비동기 체인 호출
        asyncAiService.sendDeadlineRequest(message, hubDeliveryPersonName);
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

    /* [pathHubIds 기반 허브 배송 sequenceNum 계산 메서드]
     * 허브 경로가 순서대로 들어있는 pathHybIds [hub1, hub2, hub3] 가 있는 경우
     * 허브 구간 수 = 2 (hub1→hub2, hub2→hub3)
     * 허브 간 이동 구간 수 = pathHubIds.size() - 1
     * 허브 간 이동 구간 수 없거나 1개 이하면 1로 처리
     * 업체 배송 시퀀스 번호 = hubSequence + 1 (항상 허브 배송 뒤의 마지막 단계이므로)
     */
    private int calculateHubSequenceNum(List<UUID> pathHubIds) {

        // 허브 정보 없거나, 경유 허브 없는 경우 최소 1
        if (pathHubIds == null || pathHubIds.size() < 2) {
            return 1;
        }

        // 허브 간 이동 구간 수 = 노드 수 - 1
        return pathHubIds.size() - 1;
    }
}

/* TODO
 * 배송 추적에 따라 상태 변경 로직 추가 필요 (deliveryStatus ENUM 수정 필요)
 */