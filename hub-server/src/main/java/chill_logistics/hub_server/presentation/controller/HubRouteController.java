package chill_logistics.hub_server.presentation.controller;


import chill_logistics.hub_server.application.HubRouteService;
import chill_logistics.hub_server.application.vo.HubRouteResult;
import chill_logistics.hub_server.presentation.dto.response.HubRouteResponseV1;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HubRouteController {

    private final HubRouteService hubRouteService;


    // 지울 예정
    @GetMapping("/route")
    public HubRouteResponseV1 getRoute(@RequestParam UUID startHubId, @RequestParam UUID endHubId) {

        HubRouteResult result = hubRouteService.findFastestRouteAndLog(startHubId, endHubId);

        long hours = result.totalDurationSec() / 3600;
        long minutes = (result.totalDurationSec() % 3600) / 60;

        log.info("📍 총 거리: {} km", result.totalDistanceKm());
        log.info("⏱ 예상 소요 시간: {}시간 {}분 ({}초)", hours, minutes, result.totalDurationSec());

        return new HubRouteResponseV1(
            result.startHubId(),
            result.endHubId(),
            result.pathHubIds(),
            result.totalDurationSec(),
            hours,
            minutes,
            result.totalDistanceKm()
        );
    }


}
