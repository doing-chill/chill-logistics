package chill_logistics.order_server.infrastructure.kafka.outbox;

import chill_logistics.order_server.domain.event.OutboxEventProducer;
import chill_logistics.order_server.infrastructure.kafka.KafkaPassportProducerSupport;
import chill_logistics.order_server.lib.error.ErrorCode;
import lib.passport.PassportIssuer;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderOutboxKafkaProducer implements OutboxEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PassportIssuer passportIssuer;

    @Value("${app.kafka.topic.order-after-create}")
    private String orderAfterCreateTopic;

    @Value("${app.kafka.topic.order-canceled}")
    private String orderCanceledTopic;

    @Value("${app.kafka.topic.stock-decrease}")
    private String stockDecreaseTopic;

    @Override
    public CompletableFuture<SendResult<String, String>> publish(String eventType, String key, String payload) {

        String topic = resolveTopic(eventType);

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, payload);

        // ✅ passport/user/trace 헤더 삽입
        KafkaPassportProducerSupport.writeHeaders(record.headers(), passportIssuer);

        return kafkaTemplate.send(record);
    }

    private String resolveTopic(String eventType) {

        return switch (eventType) {
            case "OrderAfterCreateV1" -> orderAfterCreateTopic;
            case "OrderCanceledV1" -> orderCanceledTopic;
            case "StockDecreaseV1" -> stockDecreaseTopic;
            default -> throw new BusinessException(ErrorCode.UNKNOWN_EVENT_TYPE);
        };
    }
}