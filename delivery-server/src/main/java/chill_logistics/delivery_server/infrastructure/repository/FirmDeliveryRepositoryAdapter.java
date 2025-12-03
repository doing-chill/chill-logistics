package chill_logistics.delivery_server.infrastructure.repository;

import chill_logistics.delivery_server.domain.entity.FirmDelivery;
import chill_logistics.delivery_server.domain.repository.FirmDeliveryRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FirmDeliveryRepositoryAdapter implements FirmDeliveryRepository {

    private final JpaFirmDeliveryRepository jpaFirmDeliveryRepository;

    @Override
    public FirmDelivery save(FirmDelivery firmDelivery) {
        return jpaFirmDeliveryRepository.save(firmDelivery);
    }

    @Override
    public Optional<FirmDelivery> findById(UUID firmDeliveryId) {
        return jpaFirmDeliveryRepository.findById(firmDeliveryId);
    }
}
