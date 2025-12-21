package chill_logistics.order_server.infrastructure.kafka;

import chill_logistics.order_server.domain.event.EventPublisher;
import chill_logistics.order_server.application.dto.command.OrderAfterCreateV1;
import lib.passport.PassportIssuer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderAfterCreateProducer implements EventPublisher {

    private final KafkaTemplate<String, OrderAfterCreateV1> orderAfterCreateKafkaTemplate;
    private final PassportIssuer passportIssuer; // ✅ 추가

    @Value("${app.kafka.topic.order-after-create}")
    private String orderAfterCreateTopic;

    @Override
    public void sendOrderAfterCreate(OrderAfterCreateV1 message) {

        String key = message.orderId().toString();

        log.info("[Kafka] OrderAfterCreate 메시지 발행, topic={}, key={}, message={}",
                orderAfterCreateTopic, key, message);

        // ✅ record로 만들어야 headers 넣을 수 있음
        ProducerRecord<String, OrderAfterCreateV1> record =
                new ProducerRecord<>(orderAfterCreateTopic, key, message);

        // ✅ passport/user/trace 헤더 삽입
        KafkaPassportProducerSupport.writeHeaders(record.headers(), passportIssuer);

        orderAfterCreateKafkaTemplate
                .send(record)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Kafka] OrderAfterCreate 메시지 전송 실패, key={}", key, ex);
                    } else {
                        log.info("[Kafka] OrderAfterCreate 메시지 전송 성공, orderId={}, offset={}",
                                key, result.getRecordMetadata().offset());
                    }
                });
    }
}