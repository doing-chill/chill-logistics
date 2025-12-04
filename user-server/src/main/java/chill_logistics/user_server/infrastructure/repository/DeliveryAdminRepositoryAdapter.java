package chill_logistics.user_server.infrastructure.repository;

import chill_logistics.user_server.domain.entity.DeliveryAdmin;
import chill_logistics.user_server.domain.repository.DeliveryAdminRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DeliveryAdminRepositoryAdapter implements DeliveryAdminRepository {

    private final JpaDeliveryAdminRepository jpaDeliveryAdminRepository;

    @Override
    public Optional<DeliveryAdmin> findById(UUID userId) {
        return jpaDeliveryAdminRepository.findById(userId);
    }
}
