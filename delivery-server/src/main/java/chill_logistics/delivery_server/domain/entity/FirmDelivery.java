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
import java.util.UUID;
import lib.entity.BaseEntity;
import lib.web.error.BusinessException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Entity
@Table(name = "p_firm_delivery")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "end_hub_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID endHubId;

    @Column(name = "receiver_firm_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID receiverFirmId;

    @Column(name = "receiver_firm_full_address", nullable = false, length = 100)
    private String receiverFirmFullAddress;

    @Column(name = "delivery_person_id", columnDefinition = "BINARY(16)")
    private UUID deliveryPersonId;

    @Column(name = "delivery_sequence_num")
    private Integer deliverySequenceNum;

    @Column(name = "receiver_firm_owner_name", length = 15)
    private String receiverFirmOwnerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", length = 15, nullable = false)
    private DeliveryStatus deliveryStatus;

    protected FirmDelivery(
        UUID orderId,
        UUID endHubId,
        UUID receiverFirmId,
        String receiverFirmFullAddress,
        String receiverFirmOwnerName,
        UUID deliveryPersonId,
        Integer deliverySequenceNum,
        DeliveryStatus deliveryStatus
    ) {
        this.orderId = orderId;
        this.endHubId = endHubId;
        this.receiverFirmId = receiverFirmId;
        this.receiverFirmFullAddress = receiverFirmFullAddress;
        this.receiverFirmOwnerName = receiverFirmOwnerName;
        this.deliveryPersonId = deliveryPersonId;
        this.deliverySequenceNum = deliverySequenceNum;
        this.deliveryStatus = deliveryStatus;
    }

    // Kafka 메시지를 기반으로 업체 배송 엔티티 생성
    public static FirmDelivery createFrom(
        HubRouteAfterCommandV1 message,
        UUID endHubId,
        UUID deliveryPersonId,
        Integer deliverySequenceNum,
        DeliveryStatus deliveryStatus
    ) {
        return new FirmDelivery(
            message.orderId(),
            endHubId, // 목적지 허브에서 시작
            message.receiverFirmId(),
//            message.receiverFirmFullAddress(),
            "업체 임시 주소",
            message.receiverFirmOwnerName(),
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
