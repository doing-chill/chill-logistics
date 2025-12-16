package chill_logistics.order_server.infrastructure.kafka;

import chill_logistics.order_server.application.dto.command.OrderStatusChangedV1;
import chill_logistics.order_server.domain.event.EventPublisher;
import chill_logistics.order_server.application.dto.command.OrderAfterCreateV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderAfterCreateProducer implements EventPublisher {

    private final KafkaTemplate<String, OrderAfterCreateV1> orderAfterCreateKafkaTemplate;
    private final KafkaTemplate<String, OrderStatusChangedV1> orderStatusChangedKafkaTemplate;

    @Value("${app.kafka.topic.order-after-create}")
    private String orderAfterCreateTopic;

    @Value("${app.kafka.topic.order-status-changed}")
    private String orderStatusChangedTopic;

    /**
     * 주문 생성 후 Kafka로 OrderAfterCreate 이벤트를 발행합니다.
     *
     * @param message 주문 생성 정보를 담고 있는 메시지 객체
     */
    @Override
    public void sendOrderAfterCreate(OrderAfterCreateV1 message) {

        String key = message.orderId().toString();

        log.info("[Kafka] OrderAfterCreate 메시지 발행, topic={}, key={}, message={}",
                orderAfterCreateTopic, key, message);

        orderAfterCreateKafkaTemplate
                .send(orderAfterCreateTopic, key, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Kafka] OrderAfterCreate 메시지 전송 실패, key={}", key, ex);
                    } else {
                        log.info("[Kafka] OrderAfterCreate 메시지 전송 성공, orderId={}, offset={}",
                                key, result.getRecordMetadata().offset());
                    }
                });
    }

    /* 여기 추가 */
    /**
     * 주문 상태 변경(취소/실패/완료 등) 시 Kafka로 OrderStatusChanged 이벤트 발행
     */
    @Override
    public void sendOrderStatusChanged(OrderStatusChangedV1 message) {

        String key = message.orderId().toString();

        log.info("[Kafka] OrderStatusChanged 메시지 발행, topic={}, key={}, message={}",
            orderStatusChangedTopic, key, message);

        orderStatusChangedKafkaTemplate
            .send(orderStatusChangedTopic, key, message)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("[Kafka] OrderStatusChanged 메시지 전송 실패, key={}", key, ex);
                } else {
                    log.info("[Kafka] OrderStatusChanged 메시지 전송 성공, orderId={}, offset={}",
                        key, result.getRecordMetadata().offset());
                }
            });
    }
}