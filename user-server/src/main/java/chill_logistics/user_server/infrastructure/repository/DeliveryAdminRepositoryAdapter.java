package chill_logistics.user_server.infrastructure.repository;

import chill_logistics.user_server.domain.entity.DeliveryAdmin;
import chill_logistics.user_server.domain.entity.DeliveryAdminType;
import chill_logistics.user_server.domain.entity.DeliveryPossibility;
import chill_logistics.user_server.domain.repository.DeliveryAdminRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DeliveryAdminRepositoryAdapter implements DeliveryAdminRepository {

    private final JpaDeliveryAdminRepository jpaDeliveryAdminRepository;

    @Override
    public Optional<DeliveryAdmin> findByUserId(UUID userId) {
        return jpaDeliveryAdminRepository.findByUserId(userId);
    }

    @Override
    public Optional<DeliveryAdmin> deliveryManagerAssign(UUID hubId, DeliveryAdminType deliveryAdminType, DeliveryPossibility deliveryPossibility) {
        return jpaDeliveryAdminRepository.findFirstByHubIdAndDeliveryAdminTypeAndDeliveryPossibilityOrderByDeliverySequenceNumAsc(hubId, deliveryAdminType, deliveryPossibility);
    }
}
