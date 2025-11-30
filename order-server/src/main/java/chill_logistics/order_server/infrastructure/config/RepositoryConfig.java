package chill_logistics.order_server.infrastructure.config;

import chill_logistics.order_server.domain.repository.OrderQueryRepository;
import chill_logistics.order_server.domain.repository.OrderRepository;
import chill_logistics.order_server.infrastructure.repository.JpaOrderQueryRepository;
import chill_logistics.order_server.infrastructure.repository.JpaOrderRepository;
import chill_logistics.order_server.infrastructure.repository.OrderQueryRepositoryAdapter;
import chill_logistics.order_server.infrastructure.repository.OrderRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public OrderRepository orderRepository(JpaOrderRepository jpaOrderRepository) {
        return new OrderRepositoryAdapter(jpaOrderRepository);
    }

    @Bean
    public OrderQueryRepository orderQueryRepository(JpaOrderQueryRepository jpaOrderQueryRepository) {
        return new OrderQueryRepositoryAdapter(jpaOrderQueryRepository);
    }
}
