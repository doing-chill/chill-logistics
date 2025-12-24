package chill_logistics.order_server.infrastructure.repository;

import chill_logistics.order_server.domain.entity.OrderOutboxEvent;
import chill_logistics.order_server.domain.entity.OrderOutboxStatus;
import chill_logistics.order_server.domain.repository.OrderOutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class OrderOutboxEventRepositoryAdapter implements OrderOutboxEventRepository {

    private final JpaOrderOutboxEventRepository jpaRepository;

    @Override
    public OrderOutboxEvent save(OrderOutboxEvent event) {
        return jpaRepository.save(event);
    }

    @Override
    public Optional<OrderOutboxEvent> findByIdForUpdate(UUID orderId) {
        return jpaRepository.findByIdForUpdate(orderId);
    }

    @Override
    public List<OrderOutboxEvent> findPendingEvents(OrderOutboxStatus status, int batchSize) {
        return jpaRepository.findPendingEvents(status, PageRequest.of(0, batchSize));
    }

    @Override
    public List<OrderOutboxEvent> findOutboxEvents(OrderOutboxStatus status, UUID orderId, int page, int size) {
        return jpaRepository.findOutboxEvents(status, orderId, PageRequest.of(page, size));
    }

    @Override
    public List<OrderOutboxEvent> findFailedEvents(int page, int size) {
        return jpaRepository.findFailedEvents(PageRequest.of(page, size));
    }
}
