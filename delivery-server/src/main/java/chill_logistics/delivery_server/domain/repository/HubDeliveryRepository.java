package chill_logistics.delivery_server.domain.repository;

import chill_logistics.delivery_server.domain.entity.HubDelivery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lib.pagination.CustomPageRequest;
import lib.pagination.CustomPageResult;

public interface HubDeliveryRepository {

    HubDelivery save(HubDelivery hubDelivery);

    Optional<HubDelivery> findById(UUID hubDeliveryId);

    CustomPageResult<HubDelivery> searchByStartHubName(String startHubName, CustomPageRequest customPageRequest);

    List<HubDelivery> findByOrderId(UUID orderId);
}
