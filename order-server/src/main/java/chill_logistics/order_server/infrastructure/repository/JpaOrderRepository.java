package chill_logistics.order_server.infrastructure.repository;

import chill_logistics.order_server.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<Order, UUID> {
}
