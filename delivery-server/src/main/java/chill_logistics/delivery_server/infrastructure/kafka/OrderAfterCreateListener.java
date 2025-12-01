package chill_logistics.delivery_server.infrastructure.kafka;

import chill_logistics.delivery_server.infrastructure.kafka.dto.OrderAfterCreateV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderAfterCreateListener {

    @KafkaListener(
        topics = "order-after-create",
        containerFactory = "orderKafkaListenerContainerFactory"
    )
    public void listen(OrderAfterCreateV1 message) {

        log.info("메시지 수신 OrderAfterCreateV1: {}", message);
    }
}
