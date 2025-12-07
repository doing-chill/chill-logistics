package chill_logistics.hub_server.application;


import chill_logistics.hub_server.application.service.HubEdgeWeightProvider;
import chill_logistics.hub_server.application.service.KakaoMapClient;
import chill_logistics.hub_server.application.vo.EdgeWeight;
import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.entity.HubInfo;
import chill_logistics.hub_server.domain.repository.HubInfoRepository;
import chill_logistics.hub_server.domain.repository.HubRepository;
import chill_logistics.hub_server.infrastructure.external.dto.response.DirectionInfoResponseV1;
import chill_logistics.hub_server.lib.error.ErrorCode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubEdgeWeightService implements HubEdgeWeightProvider {

    private final HubInfoRepository hubInfoRepository;
    private final HubRepository hubRepository;
    private final KakaoMapClient kakaoMapClient;

    @Override
    public EdgeWeight getWeight(UUID startHubId, UUID endHubId) {

        HubInfo hubInfo = hubInfoRepository.findByStartHubIdAndEndHubId(startHubId, endHubId)
            .orElseThrow(()-> new BusinessException(ErrorCode.HUB_INFO_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();

        // 1) 5분 이내의 캐시가 있으면 기존 값 그대로 사용
        if (hubInfo.getDeliveryDuration() != null && hubInfo.getDistance() != null && !hubInfo.checkUpdateTime(now)) {

            log.info("[HubEdgeWeight] cache hit: {} -> {} ({}h {}m, {} km)",
                startHubId, endHubId, hubInfo.getDeliveryDuration()/3600, hubInfo.getDeliveryDuration() % 3600 / 60, hubInfo.getDistance());

            return new EdgeWeight(startHubId, endHubId, hubInfo.getDeliveryDuration(), hubInfo.getDistance());
        }

        // 2) 아니면 Kakao API로 실제 거리/시간 계산
        // 허브 존재 여부 확인
        Hub startHub = hubRepository.findById(hubInfo.getStartHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));
        Hub endHub = hubRepository.findById(hubInfo.getEndHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        // 해당 허브들에 위도, 경도값 추출
        String origin = startHub.getLongitude() + "," + startHub.getLatitude();
        String destination = endHub.getLongitude() + "," + endHub.getLatitude();

        DirectionInfoResponseV1 direction = kakaoMapClient.getDirection(
            origin,
            destination,
            2,          // carType
            "DIESEL",   // carFuel
            true        // carHipass
        );

        // Kakao 응답은 초 단위
        int durationSec = (int) direction.duration();
        BigDecimal distanceKm = BigDecimal
            .valueOf(direction.distance() / 1000.0)
            .setScale(3, RoundingMode.HALF_UP);

        hubInfo.updateDeliveryInfo(durationSec, distanceKm);

        log.info("[HubEdgeWeight] cache hit: {} -> {} ({}h {}m, {} km)",
            startHubId, endHubId, hubInfo.getDeliveryDuration() / 3600, hubInfo.getDeliveryDuration() % 3600 / 60, hubInfo.getDistance());

        return new EdgeWeight(startHubId, endHubId, durationSec, distanceKm);
    }
}
