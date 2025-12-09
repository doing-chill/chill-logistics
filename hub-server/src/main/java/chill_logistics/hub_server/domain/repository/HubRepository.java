package chill_logistics.hub_server.domain.repository;

import chill_logistics.hub_server.domain.entity.Hub;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface HubRepository {

    boolean existsByName(String name);

    void save(Hub hub);

    List<Hub> findAll(int page, int size);

    boolean existsById(UUID hubId);

    Optional<Hub> findById(UUID hubId);

    List<Hub> findByNameOrFullAddressContaining(String nameOrFullAddress, int page, int size);

    List<Hub> findByIds(Set<UUID> hubIds);

    List<Hub> findByHubManagerId(UUID hubManagerId);

    List<Hub> findAllById(List<UUID> hubIds);

}
