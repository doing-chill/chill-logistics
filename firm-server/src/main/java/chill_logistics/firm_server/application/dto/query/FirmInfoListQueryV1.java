package chill_logistics.firm_server.application.dto.query;

import chill_logistics.firm_server.domain.entity.Firm;
import java.util.List;
import java.util.UUID;

public record FirmInfoListQueryV1 (
    UUID id,
    String name
){
    public static List<FirmInfoListQueryV1> from (List<Firm> firms) {
        return firms.stream()
            .map(firm ->  new FirmInfoListQueryV1(firm.getId(), firm.getName()))
            .toList();
    }
}
