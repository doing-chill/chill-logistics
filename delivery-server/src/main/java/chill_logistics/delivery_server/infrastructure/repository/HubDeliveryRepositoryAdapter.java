package chill_logistics.delivery_server.infrastructure.repository;

import chill_logistics.delivery_server.domain.entity.HubDelivery;
import chill_logistics.delivery_server.domain.repository.HubDeliveryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HubDeliveryRepositoryAdapter implements HubDeliveryRepository {

    private final JpaHubDeliveryRepository jpaHubDeliveryRepository;

    @Override
    public HubDelivery save(HubDelivery hubDelivery) {

        return jpaHubDeliveryRepository.save(hubDelivery);
    }
}
