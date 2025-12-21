package chill_logistics.delivery_server.infrastructure.kafka;

import chill_logistics.delivery_server.application.service.DeliveryCommandService;
import chill_logistics.delivery_server.application.dto.command.HubRouteAfterCommandV1;
import chill_logistics.delivery_server.infrastructure.kafka.dto.HubRouteAfterCreateV1;
import lib.passport.PassportValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubRouteAfterCreateListener {

    private final DeliveryCommandService deliveryCommandService;
    private final PassportValidator passportValidator;

    @KafkaListener(
            topics = "hub-route-after-create",
            containerFactory = "hubKafkaListenerContainerFactory",
            groupId = "delivery-server-hub-route-group"
    )
    public void listen(ConsumerRecord<String, HubRouteAfterCreateV1> record) {

        try {
            // ✅ 1) passport 검증 + security context 세팅
            KafkaPassportConsumerSupport.authenticate(record.headers(), passportValidator);

            HubRouteAfterCreateV1 message = record.value();
            log.info("Kafka 메시지 수신: {}", message);

            HubRouteAfterCommandV1 command = message.toCommand();
            deliveryCommandService.createDelivery(command);

        } finally {
            // ✅ 2) 반드시 clear
            KafkaPassportConsumerSupport.clear();
        }
    }
}
