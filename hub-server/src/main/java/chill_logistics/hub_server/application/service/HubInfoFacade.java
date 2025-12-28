package chill_logistics.hub_server.application.service;

import chill_logistics.hub_server.application.dto.command.CreateHubInfoCommandV1;
import chill_logistics.hub_server.application.dto.command.UpdateHubInfoCommandV1;
import chill_logistics.hub_server.application.dto.query.HubInfoQueryV1;
import chill_logistics.hub_server.application.dto.query.HubRoadInfoListQuery;
import chill_logistics.hub_server.application.dto.query.HubRoadInfoQueryV1;
import chill_logistics.hub_server.application.service.command.HubInfoCommandService;
import chill_logistics.hub_server.application.service.query.HubInfoQueryService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubInfoFacade {

    private final HubInfoQueryService hubInfoQueryService;
    private final HubInfoCommandService hubInfoCommandService;

    public void createHubInfo(CreateHubInfoCommandV1 createHubInfoCommand) {
        hubInfoCommandService.createHubInfo(createHubInfoCommand);
    }

    public HubRoadInfoQueryV1 readHubInfo(UUID hubInfoId) {
        return hubInfoQueryService.readHubInfo(hubInfoId);
    }

    public List<HubRoadInfoListQuery> readAllHubInfo(int page, int size) {
        return hubInfoQueryService.readAllHubInfo(page, size);
    }

    public void updateHubInfo(UUID hubInfoId, UpdateHubInfoCommandV1 updateHubInfoCommandV1) {
        hubInfoCommandService.updateHubInfo(hubInfoId, updateHubInfoCommandV1);
    }

    public void deleteHubInfo(UUID currentUserId, UUID hubInfoId) {
        hubInfoCommandService.deleteHubInfo(currentUserId, hubInfoId);
    }
}
