package chill_logistics.delivery_server.domain.repository;

import chill_logistics.delivery_server.domain.entity.HubDelivery;
import java.util.Optional;
import java.util.UUID;
import lib.pagination.CustomPageRequest;
import lib.pagination.CustomPageResult;

public interface HubDeliveryRepository {

    HubDelivery save(HubDelivery hubDelivery);

    Optional<HubDelivery> findById(UUID hubDeliveryId);

    CustomPageResult<HubDelivery> searchByHubName(String hubName, CustomPageRequest customPageRequest);
}
