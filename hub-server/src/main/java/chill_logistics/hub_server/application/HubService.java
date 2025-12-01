package chill_logistics.hub_server.application;

import chill_logistics.hub_server.application.dto.command.CreateHubCommandV1;
import chill_logistics.hub_server.application.dto.query.HubInfoQueryV1;
import chill_logistics.hub_server.application.dto.query.HubListQueryV1;
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
        if (hubRepository.existsByName(createHubCommand.name())){
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
    @Transactional(readOnly = true)
    public List<HubListQueryV1> readAllHub(UUID uuid, String hubName, int page, int size) {

        // user 검증 부분


        // 조회한 유저의 권한 검증


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
    public HubInfoQueryV1 readOneHub(UUID uuid, UUID hubId) {

        // user 검증 부분


        // 조회한 유저의 권한 검증

        Hub hub = hubRepository.findById(hubId)
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        // 허브 담당자의 정보를 가져와서 userName 추출 필요
        String hubManagerName = "허브 매니저 이름";

        // 허브 담당자의 이름이 필요할 거 같은데
        return HubInfoQueryV1.fromHub(hub, hubManagerName);
    }



    // 허브가 존재하는지 검증 로직
    @Transactional(readOnly = true)
    public boolean validateHub(UUID hubId) {
        return hubRepository.existsById(hubId);
    }
}
