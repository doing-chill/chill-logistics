package chill_logistics.order_server.domain.entity;

import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@Getter
@Immutable  // 조회 전용 테이블이므로 불변 선언
@Entity
@Table(name = "p_order_query")
public class OrderQuery extends BaseEntity {

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

    @Column(name = "receiver_firm_full_address", nullable = false, length = 100)
    private String receiverFirmFullAddress;

    @Column(name = "receiver_firm_owner_name", nullable = false, length = 15)
    private String receiverFirmOwnerName;

    @Column(name = "supplier_hub_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID supplierHubId;

    @Column(name = "receiver_hub_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID receiverHubId;

    @Column(name = "request_note", columnDefinition = "TEXT")
    private String requestNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 15)
    private OrderStatus orderStatus;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(name = "product_quantity", nullable = false)
    private int productQuantity;
}
