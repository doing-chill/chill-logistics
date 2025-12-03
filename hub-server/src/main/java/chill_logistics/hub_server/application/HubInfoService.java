package chill_logistics.hub_server.application;

import chill_logistics.hub_server.application.dto.command.CreateHubInfoCommandV1;
import chill_logistics.hub_server.application.dto.command.UpdateHubInfoCommandV1;
import chill_logistics.hub_server.application.dto.query.HubRoadInfoQueryV1;
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

    // CQS로 바꿔야 함
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


    public HubRoadInfoQueryV1 readHubInfo(UUID userId, UUID hubInfoId) {

        // user 검증 부분       -- 다 공통 메서드로 뺄 예정
//        UserResponseV1 user = userFeign.getUser(userId);

//        // 권한 검증
//        if (!"MASTER".equals(user.role() || !"HUB_MANAGER".equals(user.role()
//        !"DELIVERY_MANAGER".equals(user.role() || !"FIRM_MANAGER".equals(user.role())) {
//            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
//        }

        HubInfo hubInfo = hubInfoRepository.findById(hubInfoId)
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_INFO_NOT_FOUND));

        Hub startHub = hubRepository.findById(hubInfo.getStartHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        Hub endHub = hubRepository.findById(hubInfo.getEndHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        return HubRoadInfoQueryV1.from(hubInfo, startHub.getName(), endHub.getName());
    }


    public void updateHubInfo(UUID uuid, UUID hubInfoId, UpdateHubInfoCommandV1 command) {
        // 유저 검증 부분
//        UserResponseV1 user = userFeign.getUser(userId);

//        // 권한 검증: MASTER만 수정 가능
//        if (!"MASTER".equals(userInfo.role())) {
//            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
//        }

        HubInfo hubInfo = hubInfoRepository.findById(hubInfoId)
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_INFO_NOT_FOUND));

        // 이미 중복 된 연결인지 체크
        if (hubInfoRepository.existsByStartHubIdAndEndHubId(command.startHubId(), command.endHubId())) {
            throw new BusinessException(ErrorCode.HUB_ALREADY_EXISTS);
        }
        hubInfo.updateHubInfo(command.startHubId(), command.endHubId());
    }


    public void deleteHubInfo(UUID userId, UUID hubInfoId) {
        // 유저 검증 부분
//        UserResponseV1 user = userFeign.getUser(userId);

//        // 권한 검증: MASTER만 삭제 가능
//        if (!"MASTER".equals(userInfo.role())) {
//            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
//        }

        HubInfo hubInfo = hubInfoRepository.findById(hubInfoId)
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_INFO_NOT_FOUND));

        hubInfo.delete(userId);
    }



}
