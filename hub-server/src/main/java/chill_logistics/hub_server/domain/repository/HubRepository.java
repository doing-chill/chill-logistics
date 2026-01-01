package chill_logistics.hub_server.domain.repository;

import chill_logistics.hub_server.domain.entity.Hub;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lib.pagination.CustomPageRequest;
import lib.pagination.CustomPageResult;

public interface HubRepository {

    boolean existsByName(String name);

    void save(Hub hub);

    CustomPageResult<Hub> findAll(CustomPageRequest pageRequest);

    boolean existsById(UUID hubId);

    Optional<Hub> findById(UUID hubId);

    CustomPageResult<Hub> findByNameOrFullAddressContaining(String nameOrFullAddress, CustomPageRequest pageRequest);

    List<Hub> findByIds(Set<UUID> hubIds);

    List<Hub> findByHubManagerId(UUID hubManagerId);

    List<Hub> findAllById(List<UUID> hubIds);
}
