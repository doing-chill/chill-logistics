package chill_logistics.delivery_server.infrastructure.kafka.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderAfterCreateV1 {

    private UUID startHubId;

    private String startHubFullAddress;

    private UUID endHubId;

    private String endHubFullAddress;

    @Positive
    private Integer deliverySequenceNum;

    private String deliveryStatus;

    @Positive
    private Double expectedDistance;

    private LocalDateTime expectedDeliveryDuration;

    @Builder
    private OrderAfterCreateV1(
        UUID startHubId,
        String startHubFullAddress,
        UUID endHubId,
        String endHubFullAddress,
        Integer deliverySequenceNum,
        String deliveryStatus,
        Double expectedDistance,
        LocalDateTime expectedDeliveryDuration
    ) {
        this.startHubId = startHubId;
        this.startHubFullAddress = startHubFullAddress;
        this.endHubId = endHubId;
        this.endHubFullAddress = endHubFullAddress;
        this.deliverySequenceNum = deliverySequenceNum;
        this.deliveryStatus = deliveryStatus;
        this.expectedDistance = expectedDistance;
        this.expectedDeliveryDuration = expectedDeliveryDuration;
    }
}
