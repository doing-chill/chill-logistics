package chill_logistics.delivery_server.domain.entity;

import chill_logistics.delivery_server.application.dto.command.HubRouteAfterCommandV1;
import chill_logistics.delivery_server.presentation.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lib.entity.BaseEntity;
import lib.web.error.BusinessException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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

    // TODO: 삭제 예정
    @Column(name = "expected_distance", precision = 10, scale = 3)
    private BigDecimal expectedDistance;

    @Column(name = "expected_delivery_duration")
    private Integer expectedDeliveryDuration;

    // TODO: 삭제 예정
    @Column(name = "distance", precision = 10, scale = 3)
    private BigDecimal distance;

    // TODO: 삭제 예정
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
        Integer expectedDeliveryDuration,
        UUID deliveryPersonId,
        Integer deliverySequenceNum,
        DeliveryStatus deliveryStatus
    ) {
        this.orderId = orderId;
        this.startHubId = startHubId;
        this.startHubName = startHubName;
        this.startHubFullAddress = startHubFullAddress;
        this.endHubId = endHubId;
        this.endHubName = endHubName;
        this.endHubFullAddress = endHubFullAddress;
        this.expectedDeliveryDuration = expectedDeliveryDuration;
        this.deliveryPersonId = deliveryPersonId;
        this.deliverySequenceNum = deliverySequenceNum;
        this.deliveryStatus = deliveryStatus;
    }

    /* [허브 구간(startHub → endHub) 단위 row 생성용 팩토리 메서드]
     * Kafka 메시지 + Hub 정보(이름/주소)를 기반으로 허브 배송 엔티티 생성
     * pathHubIds[i] → pathHubIds[i+1] 에 해당하는 한 구간을 한 row로 만든다고 보면 된다
     */
    public static HubDelivery createFrom(
        HubRouteAfterCommandV1 message,
        UUID deliveryPersonId,
        Integer deliverySequenceNum,
        DeliveryStatus deliveryStatus) {

        return new HubDelivery(
            message.orderId(),
            message.startHubId(),
            message.startHubName(),
            message.startHubFullAddress(),
            message.endHubId(),
            message.endHubName(),
            message.endHubFullAddress(),
            message.expectedDeliveryDuration(),
            deliveryPersonId,
            deliverySequenceNum,
            deliveryStatus
        );
    }

    // 배송 상태 변경용 메서드
    public void changeStatus(DeliveryStatus nextDeliveryStatus) {

        if (!this.deliveryStatus.canTransitTo(nextDeliveryStatus)) {
            throw new BusinessException(ErrorCode.INVALID_CHANGE_DELIVERY_STATUS);
        }

        this.deliveryStatus = nextDeliveryStatus;
    }

    // 배송 취소용 메서드
    public void cancelDelivery() {

        DeliveryStatus nextDeliveryStatus = DeliveryStatus.DELIVERY_CANCELLED;

        if (!this.deliveryStatus.canTransitTo(nextDeliveryStatus)) {
            throw new BusinessException(ErrorCode.DELIVERY_ALREADY_COMPLETED_OR_CANCELED);
        }

        this.deliveryStatus = nextDeliveryStatus;
    }
}
