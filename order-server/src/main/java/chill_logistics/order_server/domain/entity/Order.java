package chill_logistics.order_server.domain.entity;

import chill_logistics.order_server.application.dto.command.OrderProductInfoV1;
import chill_logistics.order_server.lib.error.ErrorCode;
import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lib.web.error.BusinessException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuidv7")
    @GenericGenerator(
            name = "uuidv7",
            strategy = "lib.id.UUIDv7Generator"
    )
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "supplier_firm_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID supplierFirmId;

    @Column(name = "receiver_firm_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID receiverFirmId;

    @Column(name = "request_note", columnDefinition = "TEXT")
    private String requestNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 50)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProductList = new ArrayList<>();

    public static Order create(
            UUID supplierFirmId,
            UUID receiverFirmId,
            String requestNote,
            List<OrderProductInfoV1> orderProductList) {

        Order order = new Order();
        order.supplierFirmId = supplierFirmId;
        order.receiverFirmId = receiverFirmId;
        order.requestNote = requestNote;
        order.orderStatus = OrderStatus.CREATED;

        for (OrderProductInfoV1 orderProductInfo : orderProductList) {
            OrderProduct orderProduct = OrderProduct.create(orderProductInfo);
            order.addOrderProduct(orderProduct);
        }

        return order;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProductList.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public void updateStatus(OrderStatus status) {

        if (!canTransitionTo(status)) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS_TRANSITION);
        }

        this.orderStatus = status;
    }

    private boolean canTransitionTo(OrderStatus status) {
        return switch (this.orderStatus) {
            case CREATED -> status == OrderStatus.STOCK_PROCESSING || status == OrderStatus.CANCELED;
            case STOCK_PROCESSING -> status == OrderStatus.STOCK_CONFIRMED || status == OrderStatus.CANCELED;
            case STOCK_CONFIRMED -> status == OrderStatus.PROCESSING || status == OrderStatus.CANCELED;
            case PROCESSING -> status == OrderStatus.IN_TRANSIT || status == OrderStatus.CANCELED;
            case IN_TRANSIT -> status == OrderStatus.COMPLETED;
            case COMPLETED, CANCELED -> false;
        };
    }
}
