package chill_logistics.product_server.infrastructure.config.kafka;

import chill_logistics.product_server.infrastructure.kafka.dto.StockDecreaseCompletedV1;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, StockDecreaseCompletedV1> stockDecreaseCompletedProducerFactory() {

        Map<String, Object> props = new HashMap<>();

        // Kafka Broker
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // 직렬화 설정
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // 필요시 기타 옵션 (acks, retries 등) 추가

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, StockDecreaseCompletedV1> stockDecreaseCompletedKafkaTemplate() {
        return new KafkaTemplate<>(stockDecreaseCompletedProducerFactory());
    }
}