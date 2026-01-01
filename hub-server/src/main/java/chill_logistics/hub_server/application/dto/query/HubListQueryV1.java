package chill_logistics.hub_server.application.dto.query;

import chill_logistics.hub_server.domain.entity.Hub;
import java.util.List;
import lib.pagination.CustomPageResult;

public record HubListQueryV1 (
    List<HubListInfoQueryV1> contents,
    int page,
    int size,
    long totalCount,
    boolean hasNext,
    int totalPages
) {

    public static HubListQueryV1 from(CustomPageResult<Hub> hubs) {

        List<HubListInfoQueryV1> hubListQuery = hubs.getItemList()
            .stream().map(HubListInfoQueryV1::from).toList();

        return new HubListQueryV1(hubListQuery, hubs.getPage(), hubs.getSize(),
            hubs.getTotalCount(), hubs.isHasNext(), hubs.totalPages());
    }
}
