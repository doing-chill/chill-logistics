package chill_logistics.order_server.domain.repository;

import chill_logistics.order_server.domain.entity.Order;

public interface OrderRepository {
    Order save(Order order);
}
