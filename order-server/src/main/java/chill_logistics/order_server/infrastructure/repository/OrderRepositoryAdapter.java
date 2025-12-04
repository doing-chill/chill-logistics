package chill_logistics.order_server.infrastructure.repository;

import chill_logistics.order_server.domain.entity.Order;
import chill_logistics.order_server.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public Order save(Order order) {
        return jpaOrderRepository.save(order);
    }
}
