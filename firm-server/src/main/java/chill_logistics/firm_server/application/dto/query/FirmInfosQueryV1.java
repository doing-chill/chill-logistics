package chill_logistics.firm_server.application.dto.query;

import chill_logistics.firm_server.domain.entity.Firm;
import java.util.UUID;

public record FirmInfosQueryV1(
    UUID id,
    String name
) {
    public static FirmInfosQueryV1 from(Firm firm) {
        return new FirmInfosQueryV1(firm.getId(), firm.getName());
    }
}