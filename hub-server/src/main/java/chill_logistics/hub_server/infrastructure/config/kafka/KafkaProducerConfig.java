package chill_logistics.hub_server.infrastructure.config.kafka;

import chill_logistics.hub_server.infrastructure.external.dto.request.HubRouteAfterCreateV1;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;


@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServersConfig;

    @Bean
    public ProducerFactory<String, HubRouteAfterCreateV1> hubRouteAfterCreateProducerFactory() {

        Map<String, Object> props = new HashMap<>();

        // Kafka Broker
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);

        // 직렬화 설정
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // 필요시 기타 옵션 (acks, retries 등) 추가 가능

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, HubRouteAfterCreateV1> hubRouteAfterCreateKafkaTemplate() {
        return new KafkaTemplate<>(hubRouteAfterCreateProducerFactory());
    }
}