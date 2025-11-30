package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.repository.HubRouteLogRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HubRouteLogRepositoryAdapter implements HubRouteLogRepository {

    private final JpaHubRouteLogRepository jpaHubRouteLogRepository;
}
