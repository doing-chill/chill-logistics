package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.Hub;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {

    boolean findByName(String name);

    List<Hub> findByNameAndFullAddressContaining(String name, String address, PageRequest pageRequest);


    boolean existsByName(String name);
}
