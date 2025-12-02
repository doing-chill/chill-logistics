package chill_logistics.product_server.domain.repository;

import chill_logistics.product_server.domain.entity.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(UUID id);
}
