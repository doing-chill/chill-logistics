package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.Hub;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {

    Optional<Hub> findByIdAndDeletedAtIsNull(UUID id);

    Page<Hub> findByNameContainingOrFullAddressContainingAndDeletedAtIsNotNull(String name, String address, Pageable pageable);

    boolean existsByName(String name);

    boolean existsByIdAndDeletedAtIsNull(UUID id);

    Page<Hub> findAllByDeletedAtIsNull(Pageable pageable);

    List<Hub> findAllByIdInAndDeletedAtIsNull(Collection<UUID> ids);

    List<Hub> findByHubManagerIdAndDeletedAtIsNull(UUID hubManagerId);

    List<Hub> findAllByIdInAndDeletedAtIsNull(List<UUID> hubIds);
}
