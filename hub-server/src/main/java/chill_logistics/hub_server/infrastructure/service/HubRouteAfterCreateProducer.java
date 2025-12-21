package chill_logistics.hub_server.infrastructure.service;

import chill_logistics.hub_server.infrastructure.config.kafka.KafkaPassportProducerSupport;
import chill_logistics.hub_server.infrastructure.external.dto.request.HubRouteAfterCreateV1;
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
public class HubRouteAfterCreateProducer {

    private final KafkaTemplate<String, HubRouteAfterCreateV1> hubRouteAfterCreateKafkaTemplate;
    private final PassportIssuer passportIssuer;

    @Value("${app.kafka.topic.hub-route-after-create}")
    private String hubRouteAfterCreateTopic;

    public void sendHubRouteAfterCreate(HubRouteAfterCreateV1 message) {

        String key = message.orderId().toString();

        log.info("[Kafka] HubRouteAfterCreate 발행, topic={}, key={}, message={}",
                hubRouteAfterCreateTopic, key, message);

        // ✅ ProducerRecord로 만들어야 headers를 넣을 수 있음
        ProducerRecord<String, HubRouteAfterCreateV1> record =
                new ProducerRecord<>(hubRouteAfterCreateTopic, key, message);

        // ✅ passport/user/trace 헤더 삽입
        KafkaPassportProducerSupport.writeHeaders(record.headers(), passportIssuer);

        hubRouteAfterCreateKafkaTemplate
                .send(record)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Kafka] HubRouteAfterCreate 전송 실패, key={}", key, ex);
                    } else {
                        log.info("[Kafka] HubRouteAfterCreate 전송 성공, orderId={}, offset={}",
                                key, result.getRecordMetadata().offset());
                    }
                });
    }
}