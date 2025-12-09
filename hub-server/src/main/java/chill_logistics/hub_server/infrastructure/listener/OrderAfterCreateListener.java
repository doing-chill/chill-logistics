package chill_logistics.hub_server.infrastructure.listener;

import chill_logistics.hub_server.application.OrderAfterRouteOrchestrator;
import chill_logistics.hub_server.infrastructure.external.dto.response.OrderAfterCreateV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderAfterCreateListener {

    private final OrderAfterRouteOrchestrator orderAfterRouteOrchestrator;

    @KafkaListener(
        topics = "order-after-create",
        containerFactory = "orderKafkaListenerContainerFactory"
    )
    public void listen(OrderAfterCreateV1 message) {

        log.info("Kafka 메시지 수신: {}", message);

        orderAfterRouteOrchestrator.findHubRoute(message.toCommand());
    }
}