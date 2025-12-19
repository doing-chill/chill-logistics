package chill_logistics.order_server.infrastructure.config;

import chill_logistics.order_server.application.dto.command.OrderAfterCreateV1;
import chill_logistics.order_server.application.dto.command.OrderCanceledV1;
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

    /* 공통 Producer 설정 */
    private Map<String, Object> baseProducerProps() {
        Map<String, Object> props = new HashMap<>();

        // Kafka Broker
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // 직렬화 설정
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }

    /* OrderAfterCreate */
    @Bean
    public ProducerFactory<String, OrderAfterCreateV1> orderAfterCreateProducerFactory() {
        return new DefaultKafkaProducerFactory<>(baseProducerProps());
    }

    @Bean
    public KafkaTemplate<String, OrderAfterCreateV1> orderAfterCreateKafkaTemplate() {
        return new KafkaTemplate<>(orderAfterCreateProducerFactory());
    }

    /* OrderCanceled */
    @Bean
    public ProducerFactory<String, OrderCanceledV1> orderCanceledProducerFactory() {
        return new DefaultKafkaProducerFactory<>(baseProducerProps());
    }

    @Bean
    public KafkaTemplate<String, OrderCanceledV1> orderCanceledKafkaTemplate() {
        return new KafkaTemplate<>(orderCanceledProducerFactory());
    }
}
