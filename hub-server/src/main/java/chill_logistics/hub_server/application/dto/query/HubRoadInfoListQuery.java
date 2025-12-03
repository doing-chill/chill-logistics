package chill_logistics.hub_server.application.dto.query;

import java.util.UUID;

public record HubRoadInfoListQuery (
    UUID hubInfoId,
    UUID startHubId,
    String startHubName,
    UUID endHubId,
    String endHubName
){

}
