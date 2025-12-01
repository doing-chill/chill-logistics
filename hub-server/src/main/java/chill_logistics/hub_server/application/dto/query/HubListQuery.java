package chill_logistics.hub_server.application.dto.query;

import java.util.UUID;

public record HubListQuery (

    UUID hubId,
    String name,
    String fullAddress

){}
