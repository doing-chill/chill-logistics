package chill_logistics.hub_server.domain.repository;

import chill_logistics.hub_server.domain.entity.HubInfo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubInfoRepository {

    boolean existsByStartHubIdAndEndHubId(UUID startHubId, UUID endHubId);

    void save(HubInfo hubInfo);

    Optional<HubInfo> findById(UUID hubInfoId);

    List<HubInfo> findAll(int size, int page);

}
