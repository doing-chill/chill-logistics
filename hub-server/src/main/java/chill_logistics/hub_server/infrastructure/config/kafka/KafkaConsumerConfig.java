package chill_logistics.hub_server.infrastructure.config.kafka;

import chill_logistics.hub_server.infrastructure.external.dto.response.OrderAfterCreateV1;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;


@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServersConfig;

    @Bean
    public ConsumerFactory<String, OrderAfterCreateV1> orderConsumerFactory() {

        // JSON → OrderAfterCreateV1 역직렬화를 위한 Deserializer
        JsonDeserializer<OrderAfterCreateV1> deserializer = new JsonDeserializer<>(OrderAfterCreateV1.class, false);

        // Kafka 메시지 역직렬화 시 허용할 패키지를 명시적으로 지정
        deserializer.addTrustedPackages("chill_logistics.delivery_server.infrastructure.kafka.dto");

        // Kafka Consumer 설정 값
        Map<String, Object> properties = new HashMap<>();

        // Kafka Broker 주소 (Docker Compose에서 기본적으로 localhost:9092로 띄움)
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);

        // Consumer Group ID (같은 Group으로 묶인 Consumer들은 같은 메시지를 중복 처리하지 않음)
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "hub-server-group");

        // 메시지 Key 역직렬화 방식
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // 메시지 Value 역직렬화 방식 (JsonDeserializer 사용)
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // 위 설정값 + Key/Value Deserializer를 기반으로 실제 Consumer 인스턴스를 만들어 KafkaListener에 제공
        return new DefaultKafkaConsumerFactory<>(
            properties,
            new StringDeserializer(),   // Key Deserializer (String)
            deserializer                // Value Deserializer (OrderAfterCreateV1)
        );
    }

    // @KafkaListener가 사용할 Listener 컨테이너 생성 (ConsumerFactory를 주입하여 실제 Kafka Consumer 동작을 구성)
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderAfterCreateV1>
    orderKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, OrderAfterCreateV1> factory = new ConcurrentKafkaListenerContainerFactory<>();

        // ConsumerFactory 설정
        factory.setConsumerFactory(orderConsumerFactory());

        return factory;
    }
}
