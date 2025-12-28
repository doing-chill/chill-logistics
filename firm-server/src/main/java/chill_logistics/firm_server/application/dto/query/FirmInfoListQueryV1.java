package chill_logistics.firm_server.application.dto.query;

import chill_logistics.firm_server.domain.entity.Firm;
import java.util.List;
import lib.pagination.CustomPageResult;

public record FirmInfoListQueryV1 (
    List<FirmInfosQueryV1> contents,
    int page,
    int size,
    long totalCount,
    boolean hasNext,
    int totalPages
){
    public static FirmInfoListQueryV1 from (CustomPageResult<Firm> firms) {

        List<FirmInfosQueryV1> firmInfoQuery = firms.getItemList()
            .stream().map(FirmInfosQueryV1::from).toList();

        return new FirmInfoListQueryV1(firmInfoQuery, firms.getPage(), firms.getSize(),
            firms.getTotalCount(), firms.isHasNext(), firms.totalPages());
    }
}
