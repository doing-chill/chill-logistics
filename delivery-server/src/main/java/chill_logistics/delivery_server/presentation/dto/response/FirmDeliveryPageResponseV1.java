package chill_logistics.delivery_server.presentation.dto.response;

import chill_logistics.delivery_server.application.dto.query.FirmDeliverySummaryResponseV1;
import chill_logistics.delivery_server.domain.entity.FirmDelivery;
import java.util.List;
import lib.pagination.CustomPageResult;

public record FirmDeliveryPageResponseV1(
    List<FirmDeliverySummaryResponseV1> dataList,
    int page,
    int size,
    long totalCount,
    boolean hasNext,
    int totalPages) {

    public static FirmDeliveryPageResponseV1 of(
        CustomPageResult<FirmDelivery> pageResult,
        List<FirmDeliverySummaryResponseV1> dataList) {

        return new FirmDeliveryPageResponseV1(
            dataList,
            pageResult.getPage(),
            pageResult.getSize(),
            pageResult.getTotalCount(),
            pageResult.isHasNext(),
            pageResult.totalPages()
        );
    }
}
