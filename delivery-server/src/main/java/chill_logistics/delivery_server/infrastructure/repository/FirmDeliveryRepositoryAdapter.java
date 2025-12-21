package chill_logistics.delivery_server.infrastructure.repository;

import chill_logistics.delivery_server.domain.entity.FirmDelivery;
import chill_logistics.delivery_server.domain.repository.FirmDeliveryRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lib.pagination.CustomPageRequest;
import lib.pagination.CustomPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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

    @Override
    public CustomPageResult<FirmDelivery> searchByFirmOwnerName(
        String receiverOwnerName,
        CustomPageRequest customPageRequest) {

        PageRequest pageable = PageRequest.of(customPageRequest.page(), customPageRequest.size());

        Page<FirmDelivery> page;

        // 검색어 없으면 전체 조회, 있으면 조건 검색
        if (receiverOwnerName == null || receiverOwnerName.isBlank()) {
            page = jpaFirmDeliveryRepository.findByDeletedAtIsNull(pageable);
        } else {
            page = jpaFirmDeliveryRepository.findByReceiverFirmOwnerNameAndDeletedAtIsNull(receiverOwnerName, pageable);
        }

        return CustomPageResult.of(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()
        );
    }

    @Override
    public List<FirmDelivery> findByOrderId(UUID orderId) {
        return jpaFirmDeliveryRepository.findByOrderId(orderId);
    }
}
