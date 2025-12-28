package chill_logistics.hub_server.application.service.query;

import chill_logistics.hub_server.application.dto.query.HubRoadInfoListQuery;
import chill_logistics.hub_server.application.dto.query.HubRoadInfoQueryV1;
import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.entity.HubInfo;
import chill_logistics.hub_server.domain.repository.HubInfoRepository;
import chill_logistics.hub_server.domain.repository.HubRepository;
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
public class HubInfoQueryService {

    private final HubInfoRepository hubInfoRepository;
    private final HubRepository hubRepository;

    @Transactional(readOnly = true)
    public List<HubRoadInfoListQuery> readAllHubInfo(int page, int size) {

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
    public HubRoadInfoQueryV1 readHubInfo(UUID hubInfoId) {

        HubInfo hubInfo = hubInfoRepository.findById(hubInfoId)
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_INFO_NOT_FOUND));

        Hub startHub = hubRepository.findById(hubInfo.getStartHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        Hub endHub = hubRepository.findById(hubInfo.getEndHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        return HubRoadInfoQueryV1.from(hubInfo, startHub.getName(), endHub.getName());
    }
}
