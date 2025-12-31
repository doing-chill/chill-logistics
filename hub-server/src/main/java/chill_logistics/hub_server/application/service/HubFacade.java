package chill_logistics.hub_server.application.service;

import chill_logistics.hub_server.application.dto.command.CreateHubCommandV1;
import chill_logistics.hub_server.application.dto.command.UpdateHubCommandV1;
import chill_logistics.hub_server.application.dto.query.HubInfoQueryV1;
import chill_logistics.hub_server.application.dto.query.HubListInfoQueryV1;
import chill_logistics.hub_server.application.dto.query.HubListQueryV1;
import chill_logistics.hub_server.application.dto.query.UserHubsQueryV1;
import chill_logistics.hub_server.application.port.UserClient;
import chill_logistics.hub_server.application.service.command.HubCommandService;
import chill_logistics.hub_server.application.service.query.HubQueryService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubFacade {

    private final HubQueryService hubQueryService;
    private final HubCommandService hubCommandService;
    private final UserClient userClient;

    public void createHub(CreateHubCommandV1 createHubCommand) {
        // 실제 존재하는 유저인지 검증
        userClient.readUserInfo(createHubCommand.userId()).getData().to();
        hubCommandService.createHub(createHubCommand);
    }

    public void updateHub(UUID hubId, UpdateHubCommandV1 updateHubCommandV1) {
        hubCommandService.updateHub(hubId, updateHubCommandV1);
    }

    public void deleteHub(UUID currentUserId, UUID hubId) {
        hubCommandService.deleteHub(currentUserId, hubId);
    }

    public HubListQueryV1 readAllHub(String hubName, int page, int size) {
        return hubQueryService.readAllHub(hubName, page, size);
    }

    public HubInfoQueryV1 readOneHub(UUID hubId) {
        return hubQueryService.readOneHub(hubId);
    }

    public boolean validateHub(UUID hubId) {
        return hubQueryService.validateHub(hubId);
    }

    public List<UserHubsQueryV1> readUserHubs(UUID userId) {
        return hubQueryService.readUserHubs(userId);
    }
}
