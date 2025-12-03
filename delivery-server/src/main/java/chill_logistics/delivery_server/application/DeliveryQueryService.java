package chill_logistics.delivery_server.application;

import chill_logistics.delivery_server.application.dto.query.HubDeliveryInfoResponseV1;
import chill_logistics.delivery_server.application.dto.query.HubDeliverySummaryResponseV1;
import chill_logistics.delivery_server.domain.entity.HubDelivery;
import chill_logistics.delivery_server.domain.repository.HubDeliveryRepository;
import chill_logistics.delivery_server.presentation.ErrorCode;
import chill_logistics.delivery_server.presentation.dto.response.HubDeliveryPageResponseV1;
import java.util.List;
import java.util.UUID;
import lib.pagination.CustomPageRequest;
import lib.pagination.CustomPageResult;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryQueryService {

    private final HubDeliveryRepository hubDeliveryRepository;

    /* [허브 배송 단건 조회]
     */
    public HubDeliveryInfoResponseV1 getHubDelivery(UUID hubDeliveryId) {

        HubDelivery hubDelivery = hubDeliveryRepository.findById(hubDeliveryId)
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_DELIVERY_NOT_FOUND));

        if (!(hubDelivery.getDeletedAt() == null)) {
            throw new BusinessException(ErrorCode.DELIVERY_HAS_BEEN_DELETED);
        }

        return HubDeliveryInfoResponseV1.from(hubDelivery);
    }

    /* [허브 배송 검색 조회]
     * 검색 기준: startHubName
     * 검색어 없으면 전체 목록 조회, 있으면 조건 검색 결과 반환
     */
    public HubDeliveryPageResponseV1 searchHubDeliveryByHubName(String hubName, int page, int size) {

        CustomPageRequest pageRequest = new CustomPageRequest(page, size);

        // 페이징 된 엔티티 조회
        CustomPageResult<HubDelivery> entityPage = hubDeliveryRepository.searchByStartHubName(hubName, pageRequest);

        // 엔티티 → DTO로 변환
        List<HubDeliverySummaryResponseV1> dataSummaryList = entityPage.getItemList().stream()
            .map(HubDeliverySummaryResponseV1::from)
            .toList();

        return HubDeliveryPageResponseV1.of(entityPage, dataSummaryList);
    }

    /* [업체 배송 단건 조회]
     *
     */

    /* [업체 배송 검색 조회]
     *
     */
}
