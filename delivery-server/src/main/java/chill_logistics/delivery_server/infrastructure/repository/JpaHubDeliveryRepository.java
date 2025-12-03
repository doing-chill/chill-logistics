package chill_logistics.delivery_server.infrastructure.repository;

import chill_logistics.delivery_server.domain.entity.HubDelivery;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaHubDeliveryRepository extends JpaRepository<HubDelivery, UUID> {

    Page<HubDelivery> findByStartHubNameAndDeletedAtIsNull(String hubName, Pageable pageable);

    Page<HubDelivery> findByDeletedAtIsNull(Pageable pageable);
}
