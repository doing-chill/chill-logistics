package chill_logistics.hub_server.presentation.controller;


import chill_logistics.hub_server.application.service.KakaoMapClient;
import chill_logistics.hub_server.infrastructure.external.dto.response.DirectionInfoResponseV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HubRouteController {

    private final KakaoMapClient kakaoMapClient;


    @GetMapping("/route")
    public void route() {
        // ⚠️ Kakao는 "경도(lng),위도(lat)" 순서임
        String origin = "126.9779692, 37.566535";
        String destination = "129.040273,35.115111";
        Integer carType = 1;      // 소형 승용차
        String carFuel = "DIESEL";
        Boolean carHipass = true; // 하이패스 장착
        DirectionInfoResponseV1 direction = kakaoMapClient.getDirection(origin, destination,
            carType, carFuel, carHipass);

        // 거리 km 변환
        double distanceKm = direction.distance() / 1000.0;

// 소요 시간 변환
        long hours = direction.duration() / 3600;
        long minutes = (direction.duration() % 3600) / 60;

// 로그 출력
        log.info("📍 총 거리: {} km", String.format("%.2f", distanceKm));
        log.info("⏱ 예상 소요 시간: {}시간 {}분", hours, minutes);
        log.info("💰 통행료: {}원", direction.tollFare());

    }





}
