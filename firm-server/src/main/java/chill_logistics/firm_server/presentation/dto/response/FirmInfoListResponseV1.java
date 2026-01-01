package chill_logistics.firm_server.presentation.dto.response;

import chill_logistics.firm_server.application.dto.query.FirmInfoListQueryV1;
import chill_logistics.firm_server.application.dto.query.FirmInfosQueryV1;
import java.util.List;

public record FirmInfoListResponseV1 (
    List<FirmInfosQueryV1> contents,
    int page,
    int size,
    long totalCount,
    boolean hasNext,
    int totalPages
){
    public static FirmInfoListResponseV1 from(FirmInfoListQueryV1 firmInfoListQuery) {
        return new FirmInfoListResponseV1(firmInfoListQuery.contents(), firmInfoListQuery.page(),
            firmInfoListQuery.size(), firmInfoListQuery.totalCount(), firmInfoListQuery.hasNext(),
            firmInfoListQuery.totalPages());
    }
}
