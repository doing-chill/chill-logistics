package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.HubInfo;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHubInfoRepository extends JpaRepository<HubInfo, UUID> {

    boolean existsByStartHubIdAndEndHubIdAndDeletedAtIsNull(UUID startHubId, UUID endHubId);

    Optional<HubInfo> findByIdAndDeletedAtIsNull(UUID hubInfoId);

}
