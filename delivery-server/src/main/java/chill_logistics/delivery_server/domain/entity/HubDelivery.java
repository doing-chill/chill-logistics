package chill_logistics.delivery_server.domain.entity;

import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_hub_delivery")
public class HubDelivery extends BaseEntity {

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

    @Column(name = "start_hub_name", nullable = false, length = 20)
    private String startHubName;

    @Column(name = "start_hub_full_address", nullable = false, length = 100)
    private String startHubFullAddress;

    @Column(name = "end_hub_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID endHubId;

    @Column(name = "end_hub_name", nullable = false, length = 20)
    private String endHubName;

    @Column(name = "end_hub_full_address", nullable = false, length = 100)
    private String endHubFullAddress;

    @Column(name = "delivery_person_id", columnDefinition = "BINARY(16)")
    private UUID deliveryPersonId;

    @Column(name = "delivery_sequence_num")
    private Integer deliverySequenceNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", length = 15, nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(name = "expected_distance", precision = 10, scale = 3)
    private BigDecimal expectedDistance;

    @Column(name = "expected_delivery_duration")
    private Integer expectedDeliveryDuration;

    @Column(name = "distance", precision = 10, scale = 3)
    private BigDecimal distance;

    @Column(name = "delivery_duration")
    private Integer deliveryDuration;
}
