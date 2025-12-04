package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.HubInfo;
import chill_logistics.hub_server.domain.repository.HubInfoRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class HubInfoRepositoryAdapter implements HubInfoRepository {

    private final JpaHubInfoRepository jpaHubInfoRepository;

    @Override
    public boolean existsByStartHubIdAndEndHubId(UUID startHubId, UUID endHubId) {
        return jpaHubInfoRepository.existsByStartHubIdAndEndHubIdAndDeletedAtIsNull(startHubId, endHubId);
    }

    @Override
    public Optional<HubInfo> findByStartHubIdAdnEndHubId(UUID startHubId, UUID endHubId) {
        return jpaHubInfoRepository.findByStartHubIdAndEndHubIdAndDeletedAtIsNull(startHubId, endHubId);
    }

    @Override
    public void save(HubInfo hubInfo) {
        jpaHubInfoRepository.save(hubInfo);
    }

    @Override
    public Optional<HubInfo> findById(UUID hubInfoId) {
        return jpaHubInfoRepository.findByIdAndDeletedAtIsNull(hubInfoId);
    }

    @Override
    public List<HubInfo> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaHubInfoRepository.findAllByDeletedAtIsNull(pageable);
    }


}
