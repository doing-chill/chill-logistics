package chill_logistics.hub_server.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Entity
@Table(name = "p_hub_route_log")
public class HubRouteLog extends BaseEntity {

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

    @Column(name = "total_duration")
    private Integer totalDuration;
    //총 소요 시간

    @Column(name = "total_distance", precision = 10, scale = 3)
    private BigDecimal totalDistance;
    // 총 거리 km

    public static HubRouteLog create(
        UUID startHubId,
        UUID endHubId,
        Integer totalDuration,
        BigDecimal totalDistance
    ){
        HubRouteLog log = new HubRouteLog();
        log.startHubId = startHubId;
        log.endHubId = endHubId;
        log.totalDuration = totalDuration;
        log.totalDistance = totalDistance;
        return log;
    }
}
