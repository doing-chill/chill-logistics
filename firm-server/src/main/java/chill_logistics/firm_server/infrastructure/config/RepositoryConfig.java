package chill_logistics.firm_server.infrastructure.config;

import chill_logistics.firm_server.domain.repository.FirmRepository;
import chill_logistics.firm_server.infrastructure.repository.FirmRepositoryAdapter;
import chill_logistics.firm_server.infrastructure.repository.JpaFirmRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public FirmRepository firmRepository(JpaFirmRepository jpaFirmRepository) {
        return new FirmRepositoryAdapter(jpaFirmRepository);
    }
}