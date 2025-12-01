package chill_logistics.delivery_server.domain.repository;

import chill_logistics.delivery_server.domain.entity.HubDelivery;

public interface HubDeliveryRepository {

    HubDelivery save(HubDelivery hubDelivery);
}
