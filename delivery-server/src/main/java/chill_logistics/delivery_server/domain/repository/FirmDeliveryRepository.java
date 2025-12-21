package chill_logistics.delivery_server.domain.repository;

import chill_logistics.delivery_server.domain.entity.FirmDelivery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lib.pagination.CustomPageRequest;
import lib.pagination.CustomPageResult;

public interface FirmDeliveryRepository {

    FirmDelivery save(FirmDelivery firmDelivery);

    Optional<FirmDelivery> findById(UUID firmDeliveryId);

    CustomPageResult<FirmDelivery> searchByFirmOwnerName(String firmOwnerName, CustomPageRequest pageRequest);

    List<FirmDelivery> findByOrderId(UUID orderId);
}
