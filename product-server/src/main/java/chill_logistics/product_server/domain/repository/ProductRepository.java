package chill_logistics.product_server.domain.repository;

import chill_logistics.product_server.domain.entity.Product;

public interface ProductRepository {
    Product save(Product product);
}
