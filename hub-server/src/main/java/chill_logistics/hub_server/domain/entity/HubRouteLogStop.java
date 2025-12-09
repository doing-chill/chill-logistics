package chill_logistics.hub_server.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Entity
@Table(name = "p_hub_route_log_stop")
// 허브 경로 정보
public class HubRouteLogStop extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuidv7")
    @GenericGenerator(
            name = "uuidv7",
            strategy = "lib.id.UUIDv7Generator"
    )
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "hub_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID hubId;
    // 경로 허브 id

    @Column(name = "route_hub_full_address", length = 100, nullable = false)
    private String routeHubFullAddress;

    @Column(name = "hub_route_log_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID hubRouteLogId;
    // 출발허브 || 도착허브 정보

    @Column(name = "sequence_num", nullable = false)
    private Integer sequenceNum;

    public static HubRouteLogStop create(UUID hubRouteLogId, UUID hubId, int sequenceNum, String routeHubFullAddress) {
        HubRouteLogStop stop = new HubRouteLogStop();
        stop.hubRouteLogId = hubRouteLogId;
        stop.hubId = hubId;
        stop.sequenceNum = sequenceNum;
        stop.routeHubFullAddress = routeHubFullAddress;
        return stop;
    }
}



