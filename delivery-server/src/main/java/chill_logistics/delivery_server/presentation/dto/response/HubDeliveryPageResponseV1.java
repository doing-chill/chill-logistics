package chill_logistics.delivery_server.presentation.dto.response;

import chill_logistics.delivery_server.application.dto.query.HubDeliverySummaryResponseV1;
import chill_logistics.delivery_server.domain.entity.HubDelivery;
import java.util.List;
import lib.pagination.CustomPageResult;

public record HubDeliveryPageResponseV1(
    List<HubDeliverySummaryResponseV1> dataList,
    int page,
    int size,
    long totalCount,
    boolean hasNext,
    int totalPages) {

    public static  HubDeliveryPageResponseV1 of(
        CustomPageResult<HubDelivery> pageResult,
        List<HubDeliverySummaryResponseV1> dataList) {

        return new HubDeliveryPageResponseV1(
            dataList,
            pageResult.getPage(),
            pageResult.getSize(),
            pageResult.getTotalCount(),
            pageResult.isHasNext(),
            pageResult.totalPages()
        );
    }
}
