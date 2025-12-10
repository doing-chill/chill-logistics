package chill_logistics.delivery_server.application;

import chill_logistics.delivery_server.application.dto.command.AssignedDeliveryPersonV1;
import chill_logistics.delivery_server.application.dto.command.HubRouteAfterCommandV1;
import chill_logistics.delivery_server.application.dto.command.HubRouteHubInfoV1;
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
     * Kafka 메시지로 order 정보 받아와서 허브 배송 1 row 생성
     * pathHubIds를 기반으로 구간 계산 후 여러 번 호출 → N row 생성
     */
    @Transactional
    public void createHubDelivery(
        HubRouteAfterCommandV1 message,
        UUID segmentStartHubId,
        String segmentStartHubName,
        String segmentStartHubFullAddress,
        UUID segmentEndHubId,
        String segmentEndHubName,
        String segmentEndHubFullAddress,
        Integer expectedDeliveryDuration,  // 첫 구간만 값, 나머지는 null
        UUID hubDeliveryPersonId,
        int deliverySequenceNum) {

        log.info("[허브 배송 생성 시작] orderId={}, deliverySequenceNum={}",
            message.orderId(), deliverySequenceNum);

        // 초기 배송 상태 셋팅
        DeliveryStatus deliveryStatus = DeliveryStatus.WAITING_FOR_HUB;

        // HubDelivery 엔티티 생성
        HubDelivery hubDelivery = HubDelivery.createFromSegment(
            message,
            segmentStartHubId,
            segmentStartHubName,
            segmentStartHubFullAddress,
            segmentEndHubId,
            segmentEndHubName,
            segmentEndHubFullAddress,
            expectedDeliveryDuration,
            hubDeliveryPersonId,
            deliverySequenceNum,
            deliveryStatus
        );

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

        List<HubRouteHubInfoV1> pathHubs = message.pathHubs();

        if (pathHubs == null || pathHubs.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_HUB_ROUTE_PATH);
        }

        HubRouteHubInfoV1 lastHub = pathHubs.get(pathHubs.size() - 1);
        UUID endHubId = lastHub.hubId();

        // FirmDelivery 엔티티 생성
        FirmDelivery firmDelivery = FirmDelivery.createFrom(
            message,
            endHubId,
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
     * pathHubs 기반 허브 구간 (row) 여러 개 생성 + 업체 배송 생성 = 전체 배송 생성
     * 전체 배송 생성 + AI + Discord 비동기 호출
     */
    @Transactional
    public void createDelivery(HubRouteAfterCommandV1 message) {

        log.info("[배송 생성 시작] orderId={}", message.orderId());

        List<HubRouteHubInfoV1> pathHubs = message.pathHubs();

        if (pathHubs == null || pathHubs.size() < 2) {
            throw new BusinessException(ErrorCode.INVALID_HUB_ROUTE_PATH);
        }

        // pthHubs 기반 허브 구간 수 계산
        int hubSegmentCount = pathHubs.size() - 1;

        log.info("[허브 구간 수 계산] orderId={}, hubSegmentCount={}", message.orderId(), hubSegmentCount);

        // 업체 배송 담당자
        UUID lastHubId = pathHubs.get(pathHubs.size() - 1).hubId();
        AssignedDeliveryPersonV1 firmDeliveryPerson =
            deliveryPersonAssignmentService.assignFirmDeliveryPerson(lastHubId);

        UUID firmDeliveryPersonId = firmDeliveryPerson.userId();

        // AI 메시지에 넣어줄 첫 구간 허브배송 담당자 이름
        String firstHubDeliveryPersonName = null;

        // 허브 구간 수 만큼 HubDelivery row 생성
        for (int i = 0; i < hubSegmentCount; i++) {

            // 1부터 시작
            int hubDeliverySequenceNum = i + 1;

            // 첫 허브 구간에만 총 예상 소요시간 기록, 나머지는 null
            Integer segmentExpectedDeliveryDuration =
                (i == 0) ? message.expectedDeliveryDuration() : null;

            HubRouteHubInfoV1 startHub = pathHubs.get(i);
            HubRouteHubInfoV1 endHub = pathHubs.get(i + 1);

            // 각 구간 시작 허브 기준으로 허브배송 담당자 배정
            AssignedDeliveryPersonV1 hubDeliveryPerson =
                deliveryPersonAssignmentService.assignHubDeliveryPerson(startHub.hubId());

            UUID hubDeliveryPersonId = hubDeliveryPerson.userId();
            String hubDeliveryPersonName = hubDeliveryPerson.userName();

            // 첫 구간 담당자 이름은 따로 저장 (AI용)
            if (i == 0) {
                firstHubDeliveryPersonName = hubDeliveryPersonName;
            }

            createHubDelivery(
                message,
                startHub.hubId(),
                startHub.hubName(),
                startHub.hubFullAddress(),
                endHub.hubId(),
                endHub.hubName(),
                endHub.hubFullAddress(),
                segmentExpectedDeliveryDuration,
                hubDeliveryPersonId,
                hubDeliverySequenceNum
            );
        }

        // 업체 배송 sequenceNum = 마지막 허브 구간 + 1
        int firmDeliverySequenceNum = hubSegmentCount + 1;

        createFirmDelivery(message, firmDeliveryPersonId, firmDeliverySequenceNum);

        log.info(
            "[배송 생성 완료 & 배송 담당자 배정 완료] orderId={}", message.orderId());

        // AI + Discord 비동기 체인 호출
        asyncAiService.sendDeadlineRequest(message, firstHubDeliveryPersonName);
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
 * 배송 추적에 따라 상태 변경 로직 추가 필요 (deliveryStatus ENUM 수정 필요)
 */