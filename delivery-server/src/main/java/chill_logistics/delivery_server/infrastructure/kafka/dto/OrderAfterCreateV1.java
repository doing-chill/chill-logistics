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

    @NotNull
    private UUID startHubId;

//    @NotBlank
//    private String startHubName;

    @NotBlank
    private String startHubFullAddress;

    @NotNull
    private UUID endHubId;

//    @NotBlank
//    private String endHubName;

    @NotBlank
    private String endHubFullAddress;

    @NotNull
    @Positive
    private Integer deliverySequenceNum;

    @NotBlank
    private String deliveryStatus;  // ENUM String 값 (예: WAITING, IN_PROGRESS, ...)

    @NotNull
    @Positive
    private Double expectedDistance;

    @NotNull
    private LocalDateTime expectedDeliveryDuration;

    @Builder
    private OrderAfterCreateV1(
        UUID startHubId,
        String startHubName,
        String startHubFullAddress,
        UUID endHubId,
        String endHubName,
        String endHubFullAddress,
        Integer deliverySequenceNum,
        String deliveryStatus,
        Double expectedDistance,
        LocalDateTime expectedDeliveryDuration
    ) {
        this.startHubId = startHubId;
//        this.startHubName = startHubName;
        this.startHubFullAddress = startHubFullAddress;
        this.endHubId = endHubId;
//        this.endHubName = endHubName;
        this.endHubFullAddress = endHubFullAddress;
        this.deliverySequenceNum = deliverySequenceNum;
        this.deliveryStatus = deliveryStatus;
        this.expectedDistance = expectedDistance;
        this.expectedDeliveryDuration = expectedDeliveryDuration;
    }
}

/* TODO
 * Feign으로 Hub에서 받아올 것:
   * @NotBlank
     private String startHubName;
   * @NotBlank
     private String endHubName;
 */