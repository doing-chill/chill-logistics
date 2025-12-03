package chill_logistics.delivery_server.infrastructure.repository;

import chill_logistics.delivery_server.domain.entity.HubDelivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHubDeliveryRepository extends JpaRepository<HubDelivery, UUID> {

    Page<HubDelivery> findByStartHubNameAndDeletedAtIsNull(String hubName, Pageable pageable);
}
