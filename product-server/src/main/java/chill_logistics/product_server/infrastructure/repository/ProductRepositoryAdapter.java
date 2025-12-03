package chill_logistics.product_server.infrastructure.repository;

import chill_logistics.product_server.domain.entity.Product;
import chill_logistics.product_server.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaProductRepository.findById(id);
    }

    @Override
    public List<Product> searchProductList(String name, UUID firmId, UUID hubId, Boolean sellable) {
        return jpaProductRepository.searchProductList(name, firmId, hubId, sellable);
    }
}
