package chill_logistics.product_server.infrastructure.repository;

import chill_logistics.product_server.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;
}
