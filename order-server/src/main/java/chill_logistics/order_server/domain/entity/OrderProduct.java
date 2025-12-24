package chill_logistics.order_server.domain.entity;

import chill_logistics.order_server.application.dto.command.OrderProductInfoV1;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_order_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class OrderProduct {

    @Id
    @GeneratedValue(generator = "uuidv7")
    @GenericGenerator(
            name = "uuidv7",
            strategy = "lib.id.UUIDv7Generator"
    )
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "order_id",
            nullable = false,
            columnDefinition = "BINARY(16)",
            foreignKey = @ForeignKey(name = "fk_orderproduct_order")
    )
    private Order order;

    @Column(name = "product_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID productId;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(name = "product_price", nullable = false)
    private int productPrice;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status", nullable = false, length = 50)
    private StockStatus stockStatus;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public static OrderProduct create(OrderProductInfoV1 orderProductInfo) {

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.productId = orderProductInfo.productId();
        orderProduct.productName = orderProductInfo.productName();
        orderProduct.productPrice = orderProductInfo.price();
        orderProduct.quantity = orderProductInfo.quantity();
        orderProduct.stockStatus = StockStatus.STOCK_PENDING;

        return orderProduct;
    }

    protected void setOrder(Order order) {
        this.order = order;
    }

    public void markStockConfirmed() {
        this.stockStatus = StockStatus.STOCK_CONFIRMED;
    }
}
