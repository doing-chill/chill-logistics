package chill_logistics.delivery_server.infrastructure.repository;

import chill_logistics.delivery_server.domain.entity.FirmDelivery;
import chill_logistics.delivery_server.domain.repository.FirmDeliveryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FirmDeliveryRepositoryAdapter implements FirmDeliveryRepository {

    private final JpaFirmDeliveryRepository jpaFirmDeliveryRepository;

    @Override
    public FirmDelivery save(FirmDelivery firmDelivery) {

        return jpaFirmDeliveryRepository.save(firmDelivery);
    }
}
