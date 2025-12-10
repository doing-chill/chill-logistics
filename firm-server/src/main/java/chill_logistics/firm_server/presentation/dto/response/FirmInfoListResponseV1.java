package chill_logistics.firm_server.presentation.dto.response;

import chill_logistics.firm_server.application.dto.query.FirmInfoListQueryV1;
import java.util.List;
import java.util.UUID;

public record FirmInfoListResponseV1 (
    UUID id,
    String name
){
    public static List<FirmInfoListResponseV1> from(List<FirmInfoListQueryV1> queries) {
        return queries.stream()
            .map(q-> new FirmInfoListResponseV1(q.id(), q.name()))
            .toList();
    }
}
