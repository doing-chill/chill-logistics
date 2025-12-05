package chill_logistics.delivery_server.infrastructure.ai.dto.response;

public record AiDeadlineResponseV1(
    String discordMessage,   // 프롬프트 결과 전체 메시지
    String finalDeadline     // "yyyy-MM-dd HH:mm" 형태로 파싱된 최종 발송 시한 (없으면 null)
) {}
