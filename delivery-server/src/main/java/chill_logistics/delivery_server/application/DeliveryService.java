package chill_logistics.delivery_server.application;

import chill_logistics.delivery_server.domain.entity.DeliveryStatus;
import chill_logistics.delivery_server.domain.entity.HubDelivery;
import chill_logistics.delivery_server.domain.repository.FirmDeliveryRepository;
import chill_logistics.delivery_server.domain.repository.HubDeliveryRepository;
import chill_logistics.delivery_server.infrastructure.client.HubClient;
import chill_logistics.delivery_server.infrastructure.client.dto.HubForDeliveryResponseV1;
import chill_logistics.delivery_server.infrastructure.kafka.dto.OrderAfterCreateV1;
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

    /* [허브 배송 생성]
     *
     * Kafka 메시지로 order 정보 + FeignClient로 hub 정보 받아와서 허브 배송 생성
     */
    @Transactional
    public void createHubDelivery(OrderAfterCreateV1 message) {

        log.info("배송 생성 시작 - Kafka 메시지: {}", message);

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
            deliveryStatus,
            deliverySequenceNum
        );

        // 4. 허브 배송 저장
        HubDelivery savedHubDelivery = hubDeliveryRepository.save(hubDelivery);

        log.info("허브 배송 생성 완료 - hubDeliveryId={}, orderId={}",
            savedHubDelivery.getId(), savedHubDelivery.getOrderId());
    }
}

/* TODO
 * 업체 배송 생성 로직 필요
 * 배송 상태에 따라 deliveryStatus 변경 로직 필요
 */