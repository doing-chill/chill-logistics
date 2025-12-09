package chill_logistics.user_server.domain.repository;

import chill_logistics.user_server.domain.entity.DeliveryAdmin;
import chill_logistics.user_server.domain.entity.DeliveryAdminType;
import chill_logistics.user_server.domain.entity.DeliveryPossibility;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryAdminRepository {

    Optional<DeliveryAdmin> findById(UUID userId);

    Optional<DeliveryAdmin> deliveryManagerAssign(
            UUID hubId,
            DeliveryAdminType deliveryAdminType,
            DeliveryPossibility deliveryPossibility
    );
}
