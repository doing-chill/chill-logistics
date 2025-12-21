package chill_logistics.delivery_server.infrastructure.config;

import chill_logistics.delivery_server.infrastructure.kafka.dto.HubRouteAfterCreateV1;
import chill_logistics.delivery_server.infrastructure.kafka.dto.OrderCanceledV1;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

    /* 공통 Consumer 설정 */
    private Map<String, Object> baseConsumerProps(String groupId) {

        Map<String, Object> props = new HashMap<>();

        // Kafka Broker
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // Consumer Group ID
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        // Key 역직렬화
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Value 역직렬화
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // 처음 실행 시 offset 정책 (earliest: consumer group이 이 토픽의 이 파티션을 지금 들어오는 새 메시지부터 읽는다)
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return props;
    }

    /* HubRouteAfterCreate */
    @Bean
    public ConsumerFactory<String, HubRouteAfterCreateV1> hubConsumerFactory() {

        // JSON → HubRouteAfterCreateV1 역직렬화를 위한 Deserializer
        JsonDeserializer<HubRouteAfterCreateV1> deserializer =
            new JsonDeserializer<>(HubRouteAfterCreateV1.class, false);

        // Kafka 메시지 역직렬화 시 허용할 패키지를 명시적으로 지정
        deserializer.addTrustedPackages(
            "chill_logistics.delivery_server.infrastructure.kafka.dto"
        );

        // 위 설정값 + Key/Value Deserializer를 기반으로 실제 Consumer 인스턴스를 만들어 KafkaListener에 제공
        return new DefaultKafkaConsumerFactory<>(
            baseConsumerProps("delivery-server-hub-route-group"),
            new StringDeserializer(),   // Key Deserializer (String)
            deserializer                // Value Deserializer (HubRouteAfterCreateV1)
        );
    }

    /* HubRouteAfterCreate */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, HubRouteAfterCreateV1>
    // @KafkaListener가 사용할 Listener 컨테이너 생성 (ConsumerFactory를 주입하여 실제 Kafka Consumer 동작을 구성)
    hubKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, HubRouteAfterCreateV1> factory =
            new ConcurrentKafkaListenerContainerFactory<>();

        // ConsumerFactory 설정
        factory.setConsumerFactory(hubConsumerFactory());

        return factory;
    }

    /* OrderCanceled */
    @Bean
    public ConsumerFactory<String, OrderCanceledV1> orderCanceledConsumerFactory() {

        JsonDeserializer<OrderCanceledV1> deserializer =
            new JsonDeserializer<>(OrderCanceledV1.class, false);

        deserializer.addTrustedPackages(
            "chill_logistics.delivery_server.infrastructure.kafka.dto",
            "chill_logistics.delivery_server.application"  // OrderStatus ENUM 역직렬화용
        );

        return new DefaultKafkaConsumerFactory<>(
            baseConsumerProps("delivery-server-order-cancel-group"),
            new StringDeserializer(),
            deserializer
        );
    }

    /* OrderCanceled */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCanceledV1>
    orderCanceledKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, OrderCanceledV1> factory =
            new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(orderCanceledConsumerFactory());

        return factory;
    }
}
