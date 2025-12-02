package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.HubInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHubInfoRepository extends JpaRepository<HubInfo, UUID> {

    boolean existsByStartHubIdAndEndHubId(UUID startHubId, UUID endHubId);

}
