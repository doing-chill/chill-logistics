package chill_logistics.order_server.domain.entity;

import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Entity
@Table(name = "p_order")
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
    @Column(name = "order_status", nullable = false, length = 15)
    private OrderStatus orderStatus;
}
