package chill_logistics.hub_server.application.service;

import chill_logistics.hub_server.infrastructure.external.dto.response.DirectionInfoResponseV1;

public interface KakaoMapClient {


    /**
     * 카카오 내비 API를 이용해 출발지~도착지 경로의 시간/거리 조회.
     *
     * @param origin       "경도,위도,name=출발지명" 형식
     * @param destination "경도,위도,name=도착지명" 형식
     * @param carType     차종 (카카오 car_type 스펙에 맞게: 1=소형, 2=중형)
     * @param car_fuel    GASOLINE: 휘발유, DIESEL: 경유, LPG: LPG
     * @param carHipass   하이패스 장착 여부 (true/false)
     */
    DirectionInfoResponseV1 getDirection(
        String origin,
        String destination,
        Integer carType,
        String car_fuel,
        Boolean carHipass );
}