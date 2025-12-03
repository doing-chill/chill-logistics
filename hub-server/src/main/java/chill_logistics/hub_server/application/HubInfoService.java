package chill_logistics.hub_server.application;

import chill_logistics.hub_server.application.dto.command.CreateHubInfoCommandV1;
import chill_logistics.hub_server.application.dto.command.UpdateHubInfoCommandV1;
import chill_logistics.hub_server.application.dto.query.HubRoadInfoListQuery;
import chill_logistics.hub_server.application.dto.query.HubRoadInfoQueryV1;
import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.entity.HubInfo;
import chill_logistics.hub_server.domain.repository.HubInfoRepository;
import chill_logistics.hub_server.domain.repository.HubRepository;
import chill_logistics.hub_server.infrastructure.external.UserFeign;
import chill_logistics.hub_server.lib.error.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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



    @Transactional(readOnly = true)
    public List<HubRoadInfoListQuery> readAllHubInfo(UUID userId, int page, int size) {

        // user 검증 부분       -- 다 공통 메서드로 뺄 예정
//        UserResponseV1 user = userFeign.getUser(userId);

//        // 권한 검증
//        if (!"MASTER".equals(user.role() || !"HUB_MANAGER".equals(user.role()
//        !"DELIVERY_MANAGER".equals(user.role() || !"FIRM_MANAGER".equals(user.role())) {
//            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
//        }

        // offset으로 원하는 만큼 hubinfo를 가져온 후
        List<HubInfo> hubInfos = hubInfoRepository.findAll(page, size);

        // 키값을 한번에 모음
        Set<UUID> uuids = hubInfos.stream()
            .flatMap(h -> Stream.of(h.getStartHubId(), h.getEndHubId()))
            .collect(Collectors.toSet());


        // 가져온 허브들을 start와 end로 나눠서 뽑아쓰기 위해 키값인 id값으로 가져옴 (hubInfos * 2)
        List<Hub> hubs = hubRepository.findByIds(uuids);

        // 가져온 정보들을 map으로 정리
        Map<UUID, Hub> hubMap = hubs.stream()
            .collect(Collectors.toMap(Hub::getId, h -> h));

        // 응답 리스트
        List<HubRoadInfoListQuery> hubRoadInfoListQueries = new ArrayList<>();

        // 가져온 허브 정보에서 start와 end Hub로 가져와 처리
        for (HubInfo info : hubInfos) {

            Hub startHub = hubMap.get(info.getStartHubId());
            Hub endHub = hubMap.get(info.getEndHubId());

            hubRoadInfoListQueries.add(new HubRoadInfoListQuery(
                info.getId(),
                startHub.getId(), startHub.getName(),
                endHub.getId(), endHub.getName()));
        }

        return hubRoadInfoListQueries;
    }



    @Transactional(readOnly = true)
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


    @Transactional
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


    @Transactional
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
