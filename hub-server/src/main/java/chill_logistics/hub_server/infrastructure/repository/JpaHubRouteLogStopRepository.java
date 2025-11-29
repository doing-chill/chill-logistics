package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.HubRouteLogStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHubRouteLogStopRepository extends JpaRepository<HubRouteLogStop, UUID> {
}
