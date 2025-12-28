package chill_logistics.hub_server.application.service.command;

import chill_logistics.hub_server.application.dto.command.CreateHubInfoCommandV1;
import chill_logistics.hub_server.application.dto.command.UpdateHubInfoCommandV1;
import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.entity.HubInfo;
import chill_logistics.hub_server.domain.repository.HubInfoRepository;
import chill_logistics.hub_server.domain.repository.HubRepository;
import chill_logistics.hub_server.lib.error.ErrorCode;
import java.util.UUID;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubInfoCommandService {

    private final HubInfoRepository hubInfoRepository;
    private final HubRepository hubRepository;

    // 경로 생성
    @Transactional
    public void createHubInfo(CreateHubInfoCommandV1 command) {

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

    @Transactional
    public void updateHubInfo(UUID hubInfoId, UpdateHubInfoCommandV1 command) {

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

        HubInfo hubInfo = hubInfoRepository.findById(hubInfoId)
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_INFO_NOT_FOUND));

        hubInfo.delete(userId);
    }

}
