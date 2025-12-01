package chill_logistics.delivery_server.domain.entity;

import chill_logistics.delivery_server.infrastructure.kafka.dto.OrderAfterCreateV1;
import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_hub_delivery")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    protected HubDelivery(
        UUID orderId,
        UUID startHubId,
        String startHubName,
        String startHubFullAddress,
        UUID endHubId,
        String endHubName,
        String endHubFullAddress,
        Integer deliverySequenceNum,
        DeliveryStatus deliveryStatus,
        BigDecimal expectedDistance
    ) {
        this.orderId = orderId;
        this.startHubId = startHubId;
        this.startHubName = startHubName;
        this.startHubFullAddress = startHubFullAddress;
        this.endHubId = endHubId;
        this.endHubName = endHubName;
        this.endHubFullAddress = endHubFullAddress;
        this.deliverySequenceNum = deliverySequenceNum;
        this.deliveryStatus = deliveryStatus;
        this.expectedDistance = expectedDistance;
        // TODO: expectedDeliveryDuration, distance, deliveryDuration 은 아직 null 유지 (추후 계산/갱신)
    }

    // Kafka OrderAfterCreateV1 메시지를 기반으로 HubDelivery 생성
    public static HubDelivery createFrom(
        OrderAfterCreateV1 message,
        String startHubName,
        String endHubName,
        DeliveryStatus deliveryStatus
    ) {
        return new HubDelivery(
            message.orderId(),
            message.startHubId(),
            startHubName,
            message.startHubFullAddress(),
            message.endHubId(),
            endHubName,
            message.endHubFullAddress(),
            message.deliverySequenceNum(),
            deliveryStatus,
            message.expectedDistance() != null
                ? BigDecimal.valueOf(message.expectedDistance())
                : null
        );
    }
}
