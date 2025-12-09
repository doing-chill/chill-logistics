package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.HubRouteLog;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaHubRouteLogRepository extends JpaRepository<HubRouteLog, UUID> {
}
