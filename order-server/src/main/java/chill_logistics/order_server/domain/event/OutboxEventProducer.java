package chill_logistics.order_server.domain.event;

import java.util.concurrent.CompletableFuture;
import org.springframework.kafka.support.SendResult;

public interface OutboxEventProducer {

    CompletableFuture<SendResult<String, String>> publish(String eventType, String key, String payload);
}
