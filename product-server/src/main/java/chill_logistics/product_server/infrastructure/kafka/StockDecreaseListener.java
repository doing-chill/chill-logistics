package chill_logistics.product_server.infrastructure.kafka;

import chill_logistics.product_server.application.ProductFacade;
import chill_logistics.product_server.infrastructure.kafka.dto.StockDecreaseV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockDecreaseListener {

    private final ProductFacade productFacade;

    @KafkaListener(
            topics = "stock-decrease",
            containerFactory = "orderKafkaListenerContainerFactory"
    )
    public void listen(StockDecreaseV1 message) {

        log.info("Kafka 메시지 수신: {}", message);

        productFacade.decreaseStockInternalV2(message.productId(), message.quantity());
    }
}

