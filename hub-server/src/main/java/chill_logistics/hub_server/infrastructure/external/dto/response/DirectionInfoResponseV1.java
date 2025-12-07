package chill_logistics.hub_server.infrastructure.external.dto.response;

public record DirectionInfoResponseV1 (
    long duration,       // 소요 시간 (ms)
    long distance,       // 거리 (m)
    int tollFare,   // 통행 요금(톨게이트, 하이패스)
    String rawResponse  // JSON 원본
){}
