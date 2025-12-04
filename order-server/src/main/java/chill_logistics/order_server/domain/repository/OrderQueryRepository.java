package chill_logistics.order_server.domain.repository;

import chill_logistics.order_server.domain.entity.OrderQuery;

public interface OrderQueryRepository {
    void save(OrderQuery orderQuery);
}
