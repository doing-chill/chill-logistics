package chill_logistics.hub_server.application.service.command;

import chill_logistics.hub_server.application.dto.command.CreateHubCommandV1;
import chill_logistics.hub_server.application.dto.command.UpdateHubCommandV1;
import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.repository.HubRepository;
import chill_logistics.hub_server.lib.error.ErrorCode;
import java.util.UUID;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubCommandService {

    private final HubRepository hubRepository;

    @Transactional
    public void createHub(CreateHubCommandV1 createHubCommand) {

        // 이미 존재하는 허브 이름인 경우 에러
        if (hubRepository.existsByName(createHubCommand.name())){
            throw new BusinessException(ErrorCode.HUB_ALREADY_EXISTS);
        }

        Hub hub = Hub.create(createHubCommand.name(), createHubCommand.userId(), createHubCommand.postalCode(),
            createHubCommand.country(), createHubCommand.region(), createHubCommand.city(),
            createHubCommand.district(), createHubCommand.roadName(),
            createHubCommand.buildingName(), createHubCommand.detailAddress(),
            createHubCommand.fullAddress(), createHubCommand.latitude(),
            createHubCommand.longitude());
        hubRepository.save(hub);
    }

    @Transactional
    public void updateHub(UUID hubId, UpdateHubCommandV1 updateHubCommand) {
        Hub hub = hubRepository.findById(hubId)
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        hub.update(updateHubCommand.name(), updateHubCommand.hubManagerId(),
            updateHubCommand.postalCode(), updateHubCommand.country(), updateHubCommand.region(),
            updateHubCommand.city(), updateHubCommand.district(), updateHubCommand.roadName(), updateHubCommand.buildingName(),
            updateHubCommand.detailAddress(), updateHubCommand.fullAddress(), updateHubCommand.latitude(),updateHubCommand.longitude());
    }

    @Transactional
    public void deleteHub(UUID userId, UUID hubId) {
        Hub hub = hubRepository.findById(hubId)
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));
        hub.delete(userId);
    }
}
