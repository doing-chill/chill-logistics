package chill_logistics.hub_server.infrastructure.external;

import chill_logistics.hub_server.application.port.KakaoMapClient;
import chill_logistics.hub_server.infrastructure.external.dto.response.DirectionInfoResponseV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoMapImpl implements KakaoMapClient {

    private final KakaoMapFeign kakaoMapFeign;
    private final KakaoMapResponseParser kakaoMapResponseParser;

    @Override
    public DirectionInfoResponseV1 getDirection(String origin, String destination, Integer carType, String car_fuel, Boolean carHipass) {

        String responseBody = kakaoMapFeign.getDirections(origin, destination, carType, car_fuel, carHipass);

        return kakaoMapResponseParser.parse(responseBody);
    }
}
