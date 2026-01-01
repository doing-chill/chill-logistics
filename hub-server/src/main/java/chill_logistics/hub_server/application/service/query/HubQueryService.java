package chill_logistics.hub_server.application.service.query;

import chill_logistics.hub_server.application.dto.query.HubInfoQueryV1;
import chill_logistics.hub_server.application.dto.query.HubListInfoQueryV1;
import chill_logistics.hub_server.application.dto.query.HubListQueryV1;
import chill_logistics.hub_server.application.dto.query.UserHubsQueryV1;
import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.repository.HubRepository;
import chill_logistics.hub_server.lib.error.ErrorCode;
import java.util.List;
import java.util.UUID;
import lib.pagination.CustomPageRequest;
import lib.pagination.CustomPageResult;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubQueryService {

    private final HubRepository hubRepository;

    // 허브 리스트 조회
    @Transactional(readOnly = true)
    public HubListQueryV1 readAllHub(String hubName, int page, int size) {

        CustomPageRequest customPageRequest = CustomPageRequest.of(page, size, 0, 10);

        // 허브에 이름이나 주소로 검색
        CustomPageResult<Hub> pageResultHub;
        if (hubName == null || hubName.isEmpty()){
            pageResultHub = hubRepository.findAll(customPageRequest);
        }else {
            pageResultHub = hubRepository.findByNameOrFullAddressContaining
                (hubName, customPageRequest);
        }

        return HubListQueryV1.from(pageResultHub);
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
