package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.repository.HubInfoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HubInfoRepositoryAdapter implements HubInfoRepository {

    private final JpaHubInfoRepository jpaHubInfoRepository;
}
