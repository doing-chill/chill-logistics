package chill_logistics.hub_server.infrastructure.config;

import chill_logistics.hub_server.domain.repository.HubInfoRepository;
import chill_logistics.hub_server.domain.repository.HubRepository;
import chill_logistics.hub_server.domain.repository.HubRouteLogRepository;
import chill_logistics.hub_server.domain.repository.HubRouteLogStopRepository;
import chill_logistics.hub_server.infrastructure.repository.HubInfoRepositoryAdapter;
import chill_logistics.hub_server.infrastructure.repository.HubRepositoryAdapter;
import chill_logistics.hub_server.infrastructure.repository.HubRouteLogRepositoryAdapter;
import chill_logistics.hub_server.infrastructure.repository.HubRouteLogStopRepositoryAdapter;
import chill_logistics.hub_server.infrastructure.repository.JpaHubInfoRepository;
import chill_logistics.hub_server.infrastructure.repository.JpaHubRepository;
import chill_logistics.hub_server.infrastructure.repository.JpaHubRouteLogRepository;
import chill_logistics.hub_server.infrastructure.repository.JpaHubRouteLogStopRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class  RepositoryConfig {

    @Bean
    public HubRepository hubRepository(JpaHubRepository jpaHubRepository) {
        return new HubRepositoryAdapter(jpaHubRepository);
    }

    @Bean
    public HubInfoRepository hubInfoRepository(JpaHubInfoRepository jpaHubInfoRepository) {
        return new HubInfoRepositoryAdapter(jpaHubInfoRepository);
    }

    @Bean
    public HubRouteLogRepository hubRouteLogRepository(JpaHubRouteLogRepository jpaHubRouteLogRepository) {
        return new HubRouteLogRepositoryAdapter(jpaHubRouteLogRepository);
    }

    @Bean
    public HubRouteLogStopRepository hubRouteLogStopRepository(JpaHubRouteLogStopRepository jpaHubRouteLogStopRepository) {
        return new HubRouteLogStopRepositoryAdapter(jpaHubRouteLogStopRepository);
    }
}
