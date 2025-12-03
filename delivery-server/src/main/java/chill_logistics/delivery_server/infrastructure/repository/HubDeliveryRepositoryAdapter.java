package chill_logistics.delivery_server.infrastructure.repository;

import chill_logistics.delivery_server.domain.entity.HubDelivery;
import chill_logistics.delivery_server.domain.repository.HubDeliveryRepository;
import java.util.Optional;
import java.util.UUID;
import lib.pagination.CustomPageRequest;
import lib.pagination.CustomPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
public class HubDeliveryRepositoryAdapter implements HubDeliveryRepository {

    private final JpaHubDeliveryRepository jpaHubDeliveryRepository;

    @Override
    public HubDelivery save(HubDelivery hubDelivery) {
        return jpaHubDeliveryRepository.save(hubDelivery);
    }

    @Override
    public Optional<HubDelivery> findById(UUID hubDeliveryId) {
        return jpaHubDeliveryRepository.findById(hubDeliveryId);
    }

    @Override
    public CustomPageResult<HubDelivery> searchByStartHubName(
        String startHubName,
        CustomPageRequest customPageRequest) {

        PageRequest pageable = PageRequest.of(customPageRequest.page(), customPageRequest.size());

        Page<HubDelivery> page =
            jpaHubDeliveryRepository.findByStartHubNameAndDeletedAtIsNull(startHubName, pageable);

        return CustomPageResult.of(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()
        );
    }
}
