package chill_logistics.delivery_server.domain.repository;

import chill_logistics.delivery_server.domain.entity.FirmDelivery;
import java.util.Optional;
import java.util.UUID;

public interface FirmDeliveryRepository {

    FirmDelivery save(FirmDelivery firmDelivery);

    Optional<FirmDelivery> findById(UUID firmDeliveryId);
}
