package chill_logistics.delivery_server.infrastructure.kafka;

import chill_logistics.delivery_server.application.DeliveryCommandService;
import chill_logistics.delivery_server.application.dto.command.HubRouteAfterCommandV1;
import chill_logistics.delivery_server.infrastructure.kafka.dto.HubRouteAfterCreateV1;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubAfterCreateListener {

    private final DeliveryCommandService deliveryCommandService;

    @KafkaListener(
        topics = "order-after-create",
        containerFactory = "hubKafkaListenerContainerFactory"
    )
    public void listen(HubRouteAfterCreateV1 message) {

        log.info("Kafka 메시지 수신: {}", message);

        // 허브/업체 배송 담당자 배정 (임시 스텁)
        UUID hubDeliveryPersonId = assignHubDeliveryPerson(message);
        UUID firmDeliveryPersonId = assignFirmDeliveryPerson(message);

        HubRouteAfterCommandV1 command = message.toCommand();

        deliveryCommandService.createDelivery(command, hubDeliveryPersonId, firmDeliveryPersonId);
    }

    // 허브 배송 담당자 배정 (임시 버전 - 이후 배정 로직으로 교체)
    private UUID assignHubDeliveryPerson(HubRouteAfterCreateV1 message) {

        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }

    // 업체 배송 담당자 배정 (임시 버전 - 이후 배정 로직으로 교체)
    private UUID assignFirmDeliveryPerson(HubRouteAfterCreateV1 message) {

        return UUID.fromString("00000000-0000-0000-0000-000000000002"); // 임시값
    }
}

/* TODO
 * deliveryPersonId 배정 로직 구현 후 리팩터링 필요
 * startHubId 기반 담당자 조회 로직 필요
 * receiverFirmId 기반 담당자 조회 로직 필요
 */