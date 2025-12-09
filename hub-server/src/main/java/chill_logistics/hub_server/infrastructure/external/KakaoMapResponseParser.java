package chill_logistics.hub_server.infrastructure.external;


import chill_logistics.hub_server.infrastructure.external.dto.response.DirectionInfoResponseV1;
import chill_logistics.hub_server.lib.error.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoMapResponseParser {

    private final ObjectMapper objectMapper;

    public DirectionInfoResponseV1 parse(String kakaoResponse){

        try {
            JsonNode root = objectMapper.readTree(kakaoResponse);

            JsonNode routes = root.path("routes");
            if (!routes.isArray() || routes.isEmpty()) {
                log.error("[KAKAO] routes가 비어있습니다. response={}", kakaoResponse);
                throw new BusinessException(ErrorCode.KAKAO_DIRECTION_FAILED);
            }


            JsonNode summary = root.path("routes").get(0).path("summary");

            long duration = summary.path("duration").asLong();
            long distance = summary.path("distance").asLong();

            JsonNode fareNode = summary.path("fare");
            int tollFare = fareNode.path("toll").isMissingNode() ? 0 : fareNode.path("toll").asInt();


            return new DirectionInfoResponseV1(
                duration,
                distance,
                tollFare,
                kakaoResponse // raw response 그대로 저장
            );


        }catch (Exception e){
            log.error(e.getMessage());
            throw new BusinessException(ErrorCode.KAKAO_SERVER_ERROR);
        }
    }
}
