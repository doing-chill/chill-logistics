package chill_logistics.order_server.infrastructure.kafka;

import chill_logistics.order_server.application.service.OrderCommandService;
import chill_logistics.order_server.infrastructure.kafka.dto.StockDecreaseCompletedV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockDecreaseCompletedListener {

    private final OrderCommandService orderCommandService;

    @KafkaListener(
            topics = "stock-decrease-completed",
            containerFactory = "productKafkaListenerContainerFactory"
    )
    public void listen(StockDecreaseCompletedV1 message) {

        log.info("Kafka 메시지 수신: {}", message);

        orderCommandService.decreaseStockCompleted(message.orderId(), message.productId());
    }
}

