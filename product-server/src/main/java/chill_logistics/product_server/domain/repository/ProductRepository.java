package chill_logistics.product_server.domain.repository;

import chill_logistics.product_server.domain.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(UUID id);

    List<Product> readProductList(String name, UUID firmId, UUID hubId, Boolean sellable);
}
