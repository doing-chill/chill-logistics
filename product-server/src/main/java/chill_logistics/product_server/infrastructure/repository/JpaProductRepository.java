package chill_logistics.product_server.infrastructure.repository;

import chill_logistics.product_server.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {

    @Query("""
        SELECT p FROM Product p
        WHERE 
            (:name IS NULL OR p.name LIKE %:name%)
            AND (:firmId IS NULL OR p.firmId = :firmId)
            AND (:hubId IS NULL OR p.hubId = :hubId)
            AND (:sellable IS NULL OR p.sellable = :sellable)
    """
    )
    List<Product> readProductList(
            @Param("name") String name,
            @Param("firmId") UUID firmId,
            @Param("hubId") UUID hubId,
            @Param("sellable") Boolean sellable
    );

}
