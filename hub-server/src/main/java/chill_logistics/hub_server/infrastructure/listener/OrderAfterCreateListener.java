package chill_logistics.hub_server.infrastructure.listener;

import chill_logistics.hub_server.application.service.OrderAfterRouteOrchestrator;
import chill_logistics.hub_server.infrastructure.config.kafka.KafkaPassportConsumerSupport;
import chill_logistics.hub_server.infrastructure.external.dto.response.OrderAfterCreateV1;
import lib.passport.PassportValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderAfterCreateListener {

    private final OrderAfterRouteOrchestrator orderAfterRouteOrchestrator;
    private final PassportValidator passportValidator; // ✅ 추가

    @KafkaListener(
            topics = "order-after-create",
            containerFactory = "orderKafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String, OrderAfterCreateV1> record) {

        try {
            // ✅ 1) passport 검증 + security context 세팅
            KafkaPassportConsumerSupport.authenticate(record.headers(), passportValidator);

            OrderAfterCreateV1 message = record.value();
            log.info("Kafka 메시지 수신: {}", message);

            orderAfterRouteOrchestrator.findHubRoute(message.toCommand());

        } finally {
            KafkaPassportConsumerSupport.clear();
        }
    }
}