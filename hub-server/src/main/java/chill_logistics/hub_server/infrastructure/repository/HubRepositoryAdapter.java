package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HubRepositoryAdapter implements HubRepository {

    private final JpaHubRepository jpaHubRepository;
}
