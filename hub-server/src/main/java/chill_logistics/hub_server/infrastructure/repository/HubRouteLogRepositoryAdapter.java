package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.HubRouteLog;
import chill_logistics.hub_server.domain.repository.HubRouteLogRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HubRouteLogRepositoryAdapter implements HubRouteLogRepository {

    private final JpaHubRouteLogRepository jpaHubRouteLogRepository;

    @Override
    public void save(HubRouteLog hubRouteLog) {
        jpaHubRouteLogRepository.save(hubRouteLog);
    }
}
