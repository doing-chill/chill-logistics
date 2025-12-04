package chill_logistics.hub_server.domain.entity;

import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

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

    @Column(name = "hub_route_log_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID hubRouteLogId;
    // 출발허브 || 도착허브 정보

    @Column(name = "sequence_num", nullable = false)
    private Integer sequenceNum;

    public static HubRouteLogStop create(UUID hubId, int sequenceNum, UUID hubRouteLogId) {
        HubRouteLogStop stop = new HubRouteLogStop();
        stop.hubRouteLogId = hubRouteLogId;
        stop.hubId = hubId;
        stop.sequenceNum = sequenceNum;
        return stop;
    }

}



