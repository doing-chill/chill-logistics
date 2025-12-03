package chill_logistics.hub_server.domain.entity;

import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_hub_info")
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

    public void updateDeliveryInfo(Integer deliveryDuration, BigDecimal distance) {
        this.deliveryDuration = deliveryDuration;
        this.distance = distance;
    }

    public void updateHubInfo(UUID startHubId, UUID endHubId) {
        if(startHubId != null) this.startHubId = startHubId;
        if(endHubId != null) this.endHubId = endHubId;
    }

}