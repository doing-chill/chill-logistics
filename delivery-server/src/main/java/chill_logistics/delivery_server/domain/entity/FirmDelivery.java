package chill_logistics.delivery_server.domain.entity;

import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Entity
@Table(name = "p_firm_delivery")
public class FirmDelivery extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuidv7")
    @GenericGenerator(
            name = "uuidv7",
            strategy = "lib.id.UUIDv7Generator"
    )
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "order_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID orderId;

    @Column(name = "start_hub_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID startHubId;

    @Column(name = "receiver_firm_address_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID receiverFirmAddressId;

    @Column(name = "delivery_person_id", columnDefinition = "BINARY(16)")
    private UUID deliveryPersonId;

    @Column(name = "delivery_sequence_num")
    private Integer deliverySequenceNum;

    @Column(name = "receiver_firm_owner_name", length = 15)
    private String receiverFirmOwnerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", length = 15, nullable = false)
    private DeliveryStatus deliveryStatus;
}