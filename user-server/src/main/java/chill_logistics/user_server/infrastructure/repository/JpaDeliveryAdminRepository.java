package chill_logistics.user_server.infrastructure.repository;

import chill_logistics.user_server.domain.entity.DeliveryAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaDeliveryAdminRepository extends JpaRepository<DeliveryAdmin, UUID> {
}
