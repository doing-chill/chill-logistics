package chill_logistics.product_server.domain.entity;

import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Entity
@Table(name = "p_product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuidv7")
    @GenericGenerator(
            name = "uuidv7",
            strategy = "lib.id.UUIDv7Generator"
    )
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "firm_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID firmId;

    @Column(name = "hub_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID hubId;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "sellable", nullable = false)
    private boolean sellable = true;

    public boolean getSellable() {
        return sellable;
    }

    public static Product create(
            String name,
            UUID firmId,
            UUID hubId,
            int stockQuantity,
            int price,
            boolean sellable
    ) {

        Product product = new Product();
        product.name = name;
        product.firmId = firmId;
        product.hubId = hubId;
        product.stockQuantity = stockQuantity;
        product.price = price;
        product.sellable = sellable;

        return product;
    }

    public void update(
            String name,
            Integer stockQuantity,
            Integer price,
            Boolean sellable) {

        if (name != null) this.name = name;
        if (stockQuantity != null) this.stockQuantity = stockQuantity;
        if (price != null) this.price = price;
        if (sellable != null) this.sellable = sellable;
    }

    public void decreaseStock(int quantity) {
        this.stockQuantity -= quantity;
    }
}
