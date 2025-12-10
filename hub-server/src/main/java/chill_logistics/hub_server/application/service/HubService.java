package chill_logistics.hub_server.application.service;

import chill_logistics.hub_server.application.dto.command.CreateHubCommandV1;
import chill_logistics.hub_server.application.dto.command.UpdateHubCommandV1;
import chill_logistics.hub_server.application.dto.query.HubInfoQueryV1;
import chill_logistics.hub_server.application.dto.query.HubListQueryV1;
import chill_logistics.hub_server.application.dto.query.UserHubsQueryV1;
import chill_logistics.hub_server.application.dto.response.UserResponseV1;
import chill_logistics.hub_server.application.port.UserClient;
import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.repository.HubRepository;
import chill_logistics.hub_server.lib.error.ErrorCode;
import java.util.List;
import java.util.UUID;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubService {


    private final HubRepository hubRepository;
    private final UserClient userClient;

    @Transactional
    public void createHub(CreateHubCommandV1 createHubCommand) {

        // 이미 존재하는 허브 이름인 경우 에러
        if (hubRepository.existsByName(createHubCommand.name())){
            throw new BusinessException(ErrorCode.HUB_ALREADY_EXISTS);
        }

        // 실제 존재하는 유저인지 검증
        userClient.readUserInfo(createHubCommand.userId()).getData().to();

        Hub hub = Hub.create(createHubCommand.name(), createHubCommand.userId(), createHubCommand.postalCode(),
            createHubCommand.country(), createHubCommand.region(), createHubCommand.city(),
            createHubCommand.district(), createHubCommand.roadName(),
            createHubCommand.buildingName(), createHubCommand.detailAddress(),
            createHubCommand.fullAddress(), createHubCommand.latitude(),
            createHubCommand.longitude());
        hubRepository.save(hub);
    }



    // 허브 리스트 조회
    @Transactional(readOnly = true)
    public List<HubListQueryV1> readAllHub(String hubName, int page, int size) {

        // 허브에 이름이나 주소로 검색
        List<Hub> hubList;
        if (hubName == null || hubName.isEmpty()){
            hubList = hubRepository.findAll(page, size);
        }else {
            hubList =  hubRepository.findByNameOrFullAddressContaining(hubName, page, size);
        }
        return HubListQueryV1.fromHubs(hubList);
    }


    // 허브 단건 조회
    @Transactional(readOnly = true)
    public HubInfoQueryV1 readOneHub(UUID hubId) {

        Hub hub = hubRepository.findById(hubId)
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        // 허브 담당자의 정보를 가져와서 userName 추출 필요
        String hubManagerName = "허브 매니저 이름";

        // 허브 담당자의 이름이 필요할 거 같은데
        return HubInfoQueryV1.fromHub(hub, hubManagerName);
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


    @Transactional(readOnly = true)
    public boolean validateHub(UUID hubId) {
        return hubRepository.existsById(hubId);
    }

    @Transactional(readOnly = true)
    public List<UserHubsQueryV1> readUserHubs(UUID userId) {
        List<Hub> hubs = hubRepository.findByHubManagerId(userId);

        return UserHubsQueryV1.from(hubs);
    }
}
