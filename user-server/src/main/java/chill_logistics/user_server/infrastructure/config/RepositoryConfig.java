package chill_logistics.user_server.infrastructure.config;

import chill_logistics.user_server.domain.repository.DeliveryAdminRepository;
import chill_logistics.user_server.domain.repository.UserRepository;
import chill_logistics.user_server.infrastructure.repository.DeliveryAdminRepositoryAdapter;
import chill_logistics.user_server.infrastructure.repository.JpaDeliveryAdminRepository;
import chill_logistics.user_server.infrastructure.repository.JpaUserRepository;
import chill_logistics.user_server.infrastructure.repository.UserRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public UserRepository userRepository(JpaUserRepository jpaUserRepository) {
        return new UserRepositoryAdapter(jpaUserRepository);
    }

    @Bean
    public DeliveryAdminRepository deliveryAdminRepository(JpaDeliveryAdminRepository jpaDeliveryAdminRepository) {
        return new DeliveryAdminRepositoryAdapter(jpaDeliveryAdminRepository);
    }
}
