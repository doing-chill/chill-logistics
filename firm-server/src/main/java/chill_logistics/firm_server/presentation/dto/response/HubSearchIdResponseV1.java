package chill_logistics.firm_server.presentation.dto.response;

import chill_logistics.firm_server.application.dto.query.HubSearchQueryV1;
import java.util.UUID;

public record HubSearchIdResponseV1 (
    UUID hubId
){
    public static HubSearchIdResponseV1 from(HubSearchQueryV1 hubSearchQuery) {
        return new HubSearchIdResponseV1(hubSearchQuery.hubId());
    }
}
