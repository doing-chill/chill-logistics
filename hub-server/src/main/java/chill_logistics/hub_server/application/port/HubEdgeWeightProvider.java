package chill_logistics.hub_server.application.port;

import chill_logistics.hub_server.application.vo.EdgeWeight;
import java.util.UUID;

public interface HubEdgeWeightProvider {


    /**
     * 허브 간 엣지 가중치 (시간/거리) 제공
     * - 5분 이내 캐시값 있으면 재사용
     * - 아니면 Kakao API로 실시간 계산 후 캐시 갱신
     */
    EdgeWeight getWeight(UUID startHubId, UUID endHubId);
}