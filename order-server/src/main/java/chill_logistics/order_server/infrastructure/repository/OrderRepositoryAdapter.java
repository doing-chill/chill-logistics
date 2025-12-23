package chill_logistics.order_server.infrastructure.repository;

import chill_logistics.order_server.domain.entity.Order;
import chill_logistics.order_server.domain.entity.OrderStatus;
import chill_logistics.order_server.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public Order save(Order order) {
        return jpaOrderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return jpaOrderRepository.findById(orderId);
    }

    @Override
    public List<Order> readOrderList(UUID supplierFirmId, UUID receiverFirmId, OrderStatus orderStatus) {
        return jpaOrderRepository.readOrderList(supplierFirmId, receiverFirmId, orderStatus);
    }

    @Override
    public Optional<Order> findByIdForUpdate(UUID orderId) {
        return jpaOrderRepository.findByIdForUpdate(orderId);
    }
}
