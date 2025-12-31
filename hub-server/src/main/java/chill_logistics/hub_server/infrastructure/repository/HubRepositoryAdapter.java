package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.repository.HubRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lib.pagination.CustomPageRequest;
import lib.pagination.CustomPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class HubRepositoryAdapter implements HubRepository {

    private final JpaHubRepository jpaHubRepository;

    @Override
    public boolean existsByName(String name) {
        return jpaHubRepository.existsByName(name);
    }

    @Override
    public void save(Hub hub) {
        jpaHubRepository.save(hub);
    }

    @Override
    public CustomPageResult<Hub> findAll(CustomPageRequest pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.page(), pageRequest.size(), Sort.by("name").ascending());

        Page<Hub> readAllHub = jpaHubRepository.findAllByDeletedAtIsNull(pageable);

        return CustomPageResult.of(
            readAllHub.getContent(),
            readAllHub.getNumber(),
            readAllHub.getSize(),
            readAllHub.getTotalElements()
            );
    }

    @Override
    public boolean existsById(UUID hubId) {
        return jpaHubRepository.existsByIdAndDeletedAtIsNull(hubId);
    }

    @Override
    public Optional<Hub> findById(UUID hubId) {
        return jpaHubRepository.findByIdAndDeletedAtIsNull(hubId);
    }

    @Override
    public CustomPageResult<Hub> findByNameOrFullAddressContaining(String nameOrFullAddress, CustomPageRequest pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.page(), pageRequest.size());

        Page<Hub> readHubSearchByName = jpaHubRepository.findByNameContainingOrFullAddressContainingAndDeletedAtIsNotNull(
            nameOrFullAddress, nameOrFullAddress, pageable);

        return CustomPageResult.of(
            readHubSearchByName.getContent(),
            readHubSearchByName.getNumber(),
            readHubSearchByName.getSize(),
            readHubSearchByName.getTotalElements()
        );
    }

    @Override
    public List<Hub> findByIds(Set<UUID> hubIds) {
        return jpaHubRepository.findAllByIdInAndDeletedAtIsNull(hubIds);
    }

    @Override
    public List<Hub> findByHubManagerId(UUID hubManagerId) {
        return jpaHubRepository.findByHubManagerIdAndDeletedAtIsNull(hubManagerId);
    }

    @Override
    public List<Hub> findAllById(List<UUID> hubIds) {
        return jpaHubRepository.findAllByIdInAndDeletedAtIsNull(hubIds);
    }
}
