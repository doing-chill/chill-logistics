package chill_logistics.order_server.domain.repository;

import chill_logistics.order_server.domain.entity.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID orderId);
}
