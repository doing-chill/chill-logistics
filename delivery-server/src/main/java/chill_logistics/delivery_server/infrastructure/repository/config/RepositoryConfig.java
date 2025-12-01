package chill_logistics.delivery_server.infrastructure.repository.config;

import chill_logistics.delivery_server.domain.repository.FirmDeliveryRepository;
import chill_logistics.delivery_server.domain.repository.HubDeliveryRepository;
import chill_logistics.delivery_server.infrastructure.repository.FirmDeliveryRepositoryAdapter;
import chill_logistics.delivery_server.infrastructure.repository.HubDeliveryRepositoryAdapter;
import chill_logistics.delivery_server.infrastructure.repository.JpaFirmDeliveryRepository;
import chill_logistics.delivery_server.infrastructure.repository.JpaHubDeliveryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public HubDeliveryRepository hubDeliveryRepository(JpaHubDeliveryRepository jpaHubDeliveryRepository) {
        return new HubDeliveryRepositoryAdapter(jpaHubDeliveryRepository);
    }

    @Bean
    public FirmDeliveryRepository firmDeliveryRepository(JpaFirmDeliveryRepository jpaFirmDeliveryRepository) {
        return new FirmDeliveryRepositoryAdapter(jpaFirmDeliveryRepository);
    }
}