package chill_logistics.hub_server.infrastructure.listener;

import chill_logistics.hub_server.application.dto.command.OrderAfterCommandV1;
import chill_logistics.hub_server.infrastructure.external.dto.request.OrderAfterCreateV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderAfterCreateListener {

    private final HubCommandService hubCommandService;

    @KafkaListener(
        topics = "order-after-create",
        containerFactory = "orderKafkaListenerContainerFactory"
    )
    public void listen(OrderAfterCreateV1 message) {

        log.info("Kafka 메시지 수신: {}", message);

        // DTO → Command 변환
        OrderAfterCommandV1 command = message.toCommand();

        hubCommandService.calculateExpectedDuration(command);
    }
}