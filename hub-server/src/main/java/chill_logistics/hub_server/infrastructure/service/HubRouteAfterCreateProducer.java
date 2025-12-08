package chill_logistics.hub_server.infrastructure.service;

import chill_logistics.hub_server.infrastructure.external.dto.response.HubRouteAfterCreateV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubRouteAfterCreateProducer {

    private final KafkaTemplate<String, HubRouteAfterCreateV1> hubRouteAfterCreateKafkaTemplate;

    @Value("${app.kafka.topic.hub-route-after-create}")
    private String hubRouteAfterCreateTopic;

    /* [허브 경로 생성 후 Kafka로 HubRouteAfterCreate 이벤트 발행]
     */
    public void sendHubRouteAfterCreate(HubRouteAfterCreateV1 message) {

        String key = message.orderId().toString();

        log.info("[Kafka] HubRouteAfterCreate 메시지 발행, topic={}, key={}, message={}",
            hubRouteAfterCreateTopic, key, message);

        hubRouteAfterCreateKafkaTemplate
            .send(hubRouteAfterCreateTopic, key, message)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("[Kafka] HubRouteAfterCreate 메시지 전송 실패, key={}", key, ex);
                } else {
                    log.info("[Kafka] HubRouteAfterCreate 메시지 전송 성공, orderId={}, offset={}",
                        key, result.getRecordMetadata().offset());
                }
            });
    }
}