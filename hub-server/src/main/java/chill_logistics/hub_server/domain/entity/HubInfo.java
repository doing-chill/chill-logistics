package chill_logistics.hub_server.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Entity
@Table(
    name = "p_hub_info",
    indexes = {
        @Index(
            name = "uk_start_end_hub",
            columnList = "start_hub_id, end_hub_id",
            unique = true
        )
    }
)
public class HubInfo extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuidv7")
    @GenericGenerator(
            name = "uuidv7",
            strategy = "lib.id.UUIDv7Generator"
    )
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "start_hub_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID startHubId;

    @Column(name = "end_hub_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID endHubId;

    @Column(name = "delivery_duration")
    private Integer deliveryDuration;
    // 배송 예측 소요 시간

    @Column(name = "distance", precision = 10, scale = 3)
    private BigDecimal distance;
    // 배송 거리 km 단위

    public static HubInfo create(UUID startHubId, UUID endHubId) {
        HubInfo hubInfo = new HubInfo();
        hubInfo.startHubId = startHubId;
        hubInfo.endHubId = endHubId;
        return hubInfo;
    }

    // 시간, 거리 넣기
    public void updateDeliveryInfo(Integer deliveryDuration, BigDecimal distance) {
        this.deliveryDuration = deliveryDuration;
        this.distance = distance;
    }


    // false면 기존 값 재사용
    public boolean checkUpdateTime(LocalDateTime updateTime) {
        LocalDateTime updatedAt = this.getUpdatedAt();
        if (updatedAt == null) {
            return true;
        }

        if (this.deliveryDuration == null || this.distance == null) {
           return true;
       }
        // 두 시간 차이를 분 단위로 계산
        long diffMinutes = Duration.between(updatedAt, updateTime).toMinutes();

        // 5분 이상 차이가 난다면 true
        return diffMinutes >= 5;

        // TODO 캐시 스탬피드 현상 측정을 위해 넣음
        // 두 시간 차이를 분 단위로 계산
//        long time = Duration.between(updatedAt, updateTime).toSeconds();
//
//        // 5초 차이가 난다면 true
//        return time >= 5;

    }

    public void updateHubInfo(UUID startHubId, UUID endHubId) {
        if(startHubId != null) this.startHubId = startHubId;
        if(endHubId != null) this.endHubId = endHubId;
    }
}