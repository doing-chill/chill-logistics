package chill_logistics.order_server.infrastructure.repository;

import chill_logistics.order_server.domain.entity.OrderQuery;
import chill_logistics.order_server.domain.repository.OrderQueryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderQueryRepositoryAdapter implements OrderQueryRepository {

    private final JpaOrderQueryRepository jpaOrderQueryRepository;

    @Override
    public void save(OrderQuery orderQuery) {
        jpaOrderQueryRepository.save(orderQuery);
    }
}
