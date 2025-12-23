package chill_logistics.order_server.domain.repository;

import chill_logistics.order_server.domain.entity.OrderOutboxEvent;
import chill_logistics.order_server.domain.entity.OrderOutboxStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderOutboxEventRepository {

    OrderOutboxEvent save(OrderOutboxEvent orderOutboxEvent);

    Optional<OrderOutboxEvent> findById(UUID orderOutboxEventId);

    List<OrderOutboxEvent> findPendingEvents(OrderOutboxStatus status, int batchSize);

    List<OrderOutboxEvent> findOutboxEvents(OrderOutboxStatus status, UUID orderId, int page, int size);

    List<OrderOutboxEvent> findFailedEvents(int page, int size);

    int resetFailedToPending(List<UUID> ids);
}
