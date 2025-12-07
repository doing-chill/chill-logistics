package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.HubRouteLogStop;
import chill_logistics.hub_server.domain.repository.HubRouteLogStopRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HubRouteLogStopRepositoryAdapter implements HubRouteLogStopRepository {

    private final JpaHubRouteLogStopRepository jpaHubRouteLogStopRepository;

    @Override
    public void save(HubRouteLogStop hubRouteLogStop) {
        jpaHubRouteLogStopRepository.save(hubRouteLogStop);
    }
}
