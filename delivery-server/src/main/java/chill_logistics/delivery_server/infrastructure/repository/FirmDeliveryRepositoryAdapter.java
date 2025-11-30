package chill_logistics.delivery_server.infrastructure.repository;

import chill_logistics.delivery_server.domain.repository.FirmDeliveryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FirmDeliveryRepositoryAdapter implements FirmDeliveryRepository {

    private final JpaFirmDeliveryRepository jpaFirmDeliveryRepository;
}
