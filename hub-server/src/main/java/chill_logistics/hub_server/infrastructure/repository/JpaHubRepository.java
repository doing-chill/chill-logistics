package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.Hub;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {

    Optional<Hub> findByIdAndDeletedAtIsNull(UUID id);

    List<Hub> findByNameAndFullAddressContainingAndDeletedAtIsNotNull(String name, String address, Pageable pageable);

    boolean existsByName(String name);

    List<Hub> findAllByDeletedAtIsNull(Pageable pageable);

    List<Hub> findAllByIdInAndDeletedAtIsNull(Collection<UUID> ids);
}
