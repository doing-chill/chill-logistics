package chill_logistics.hub_server.application;

import chill_logistics.hub_server.application.dto.command.CreateHubCommandV1;
import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.repository.HubRepository;
import chill_logistics.hub_server.lib.error.ErrorCode;
import chill_logistics.hub_server.presentation.dto.response.HubInfoResponseV1;
import chill_logistics.hub_server.presentation.dto.response.HubListResponseV1;
import java.util.List;
import java.util.UUID;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubService {

    //facade 패턴으로 변경 예정

    private final HubRepository hubRepository;

    @Transactional
    public void createHub(UUID userId, CreateHubCommandV1 createHubCommand) {


        // 유저 검증 부분


//        // 권한 검증: MASTER만 허브 생성 가능
//        if (!"MASTER".equals(userInfo.role())) {
//            log.warn(userInfo.role());
//            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
//        }

        // 이미 존재하는 허브 이름인 경우 에러
        if (hubRepository.findByName(createHubCommand.name())){
            throw new BusinessException(ErrorCode.HUB_NOT_FOUND);
        }


        // 현재 로그인한 사용자를 허브 관리자로 설정
        //UUID hubManagerId = userInfo.userId(); 이게 맞지만 user가 없어서
        UUID hubManagerId = userId;

        Hub hub = Hub.create(createHubCommand.name(), userId, createHubCommand.postalCode(),
            createHubCommand.country(), createHubCommand.region(), createHubCommand.city(),
            createHubCommand.district(), createHubCommand.roadName(),
            createHubCommand.buildingName(), createHubCommand.detailAddress(),
            createHubCommand.fullAddress(), createHubCommand.latitude(),
            createHubCommand.longitude());
        hubRepository.save(hub);
    }

    // 허브 리스트 조회
    public List<HubListResponseV1> readAllHub(UUID uuid, String hubName, int page, int size) {

        // user 검증 부분

        if (hubName == null || hubName.isEmpty()){
            hubRepository.
        }




    }


    // 허브 단건 조회
    @GetMapping("/{hubId}")
    public boolean validateHub(@PathVariable UUID hubId) {

        return hubService.validateHub(hubId);
    }


    public HubInfoResponseV1 readOneHub(UUID uuid, UUID hubId) {
    }
}
