package chill_logistics.delivery_server.infrastructure.kafka;

import chill_logistics.delivery_server.application.DeliveryService;
import chill_logistics.delivery_server.infrastructure.kafka.dto.OrderAfterCreateV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderAfterCreateListener {

    private final DeliveryService deliveryService;

    @KafkaListener(
        topics = "order-after-create",
        containerFactory = "orderKafkaListenerContainerFactory"
    )
    public void listen(OrderAfterCreateV1 message) {

        log.info("메시지 수신 OrderAfterCreateV1: {}", message);

        deliveryService.createDelivery(message);
    }
}
