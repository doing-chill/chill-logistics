package chill_logistics.delivery_server.infrastructure.kafka;

import chill_logistics.delivery_server.application.DeliveryCommandService;
import chill_logistics.delivery_server.application.dto.command.HubRouteAfterCommandV1;
import chill_logistics.delivery_server.infrastructure.kafka.dto.HubRouteAfterCreateV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubRouteAfterCreateListener {

    private final DeliveryCommandService deliveryCommandService;

    @KafkaListener(
        topics = "hub-route-after-create",
        containerFactory = "hubKafkaListenerContainerFactory"
    )
    public void listen(HubRouteAfterCreateV1 message) {

        log.info("Kafka 메시지 수신: {}", message);

        HubRouteAfterCommandV1 command = message.toCommand();

        deliveryCommandService.createDelivery(command);
    }
}
