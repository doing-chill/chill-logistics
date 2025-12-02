package chill_logistics.hub_server.application;

import chill_logistics.hub_server.application.dto.command.CreateHubInfoCommandV1;
import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.entity.HubInfo;
import chill_logistics.hub_server.domain.repository.HubInfoRepository;
import chill_logistics.hub_server.domain.repository.HubRepository;
import chill_logistics.hub_server.infrastructure.external.UserFeign;
import chill_logistics.hub_server.lib.error.ErrorCode;
import java.util.UUID;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubInfoService {

    private final HubInfoRepository hubInfoRepository;
    private final HubRepository hubRepository;
    private final UserFeign userFeign;


    // 경로 생성
    @Transactional
    public void createHubInfo(UUID userId, CreateHubInfoCommandV1 command) {

        // 유저 검증 부분
//        UserResponseV1 user = userFeign.getUser(userId);

//        // 권한 검증: MASTER만 생성 가능
//        if (!"MASTER".equals(userInfo.role())) {
//            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
//        }

        // 허브 존재 여부 확인
        Hub startHub = hubRepository.findById(command.startHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));
        Hub endHub = hubRepository.findById(command.endHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));


        // 이미 중복 된 연결인지 체크
        if (hubInfoRepository.existsByStartHubIdAndEndHubId(startHub.getId(), endHub.getId())){
            throw new BusinessException(ErrorCode.HUB_ALREADY_EXISTS);
        }

        HubInfo hubInfo = HubInfo.create(startHub.getId(), endHub.getId());
        hubInfoRepository.save(hubInfo);
    }




}
