package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.HubInfo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaHubInfoRepository extends JpaRepository<HubInfo, UUID> {

    boolean existsByStartHubIdAndEndHubIdAndDeletedAtIsNull(UUID startHubId, UUID endHubId);

    Optional<HubInfo> findByStartHubIdAndEndHubIdAndDeletedAtIsNull(UUID startHubId, UUID endHubId);

    Optional<HubInfo> findByIdAndDeletedAtIsNull(UUID hubInfoId);

    List<HubInfo> findAllByDeletedAtIsNull(Pageable pageable);

    List<HubInfo> findByDeletedAtIsNull();

}
