package chill_logistics.user_server.infrastructure.repository;

import chill_logistics.user_server.domain.entity.DeliveryAdmin;
import chill_logistics.user_server.domain.entity.DeliveryAdminType;
import chill_logistics.user_server.domain.entity.DeliveryPossibility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaDeliveryAdminRepository extends JpaRepository<DeliveryAdmin, UUID> {

    Optional<DeliveryAdmin> findFirstByHubIdAndDeliveryAdminTypeAndDeliveryPossibilityOrderByDeliverySequenceNumAsc(
            UUID hubId,
            DeliveryAdminType deliveryAdminType,
            DeliveryPossibility deliveryPossibility
    );

    Optional<DeliveryAdmin> findByUserId(UUID userId);
}
