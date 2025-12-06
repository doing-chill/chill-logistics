package chill_logistics.user_server.domain.repository;

import chill_logistics.user_server.domain.entity.DeliveryAdmin;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryAdminRepository {

    Optional<DeliveryAdmin> findById(UUID userId);
}
