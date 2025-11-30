package chill_logistics.product_server.infrastructure.config;

import chill_logistics.product_server.domain.repository.ProductRepository;
import chill_logistics.product_server.infrastructure.repository.JpaProductRepository;
import chill_logistics.product_server.infrastructure.repository.ProductRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public ProductRepository productRepository(JpaProductRepository jpaProductRepository) {
        return new ProductRepositoryAdapter(jpaProductRepository);
    }
}