package chill_logistics.product_server.infrastructure.kafka;

import chill_logistics.product_server.infrastructure.kafka.dto.StockDecreaseCompletedV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockDecreaseCompletedProducer {

    private final KafkaTemplate<String, StockDecreaseCompletedV1> StockDecreaseCompletedKafkaTemplate;

    @Value("${app.kafka.topic.stock-decrease-completed}")
    private String stockDecreaseCompletedTopic;

    /* [주문 생성 후 Kafka로 StockDecreaseCompleted 이벤트 발행]
     */
    public void sendStockDecreaseCompleted(StockDecreaseCompletedV1 message) {

        String key = message.orderId().toString();

        log.info("[Kafka] StockDecreaseCompleted 메시지 발행, topic={}, key={}, message={}",
                stockDecreaseCompletedTopic, key, message);

        StockDecreaseCompletedKafkaTemplate
                .send(stockDecreaseCompletedTopic, key, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Kafka] StockDecreaseCompleted 메시지 전송 실패, key={}", key, ex);
                    } else {
                        log.info("[Kafka] StockDecreaseCompleted 메시지 전송 성공, orderId={}, offset={}",
                                key, result.getRecordMetadata().offset());
                    }
                });
    }
}
