package chill_logistics.delivery_server.application;

import chill_logistics.delivery_server.application.dto.command.HubRouteAfterCommandV1;
import chill_logistics.delivery_server.infrastructure.ai.AiClient;
import chill_logistics.delivery_server.infrastructure.ai.dto.request.AiDeadlineRequestV1;
import chill_logistics.delivery_server.infrastructure.ai.dto.response.AiDeadlineResponseV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncAiService {

    private final AiClient aiClient;

    /* [배송 최종 발송 시한 계산 요청]
     * HubRouteAfterCommandV1 메시지를 기반으로 AI에 비동기 호출
     * HubRouteAfterCommandV1 → AiDeadlineRequestV1 로 변환 (AI에게 줄 데이터 정제)
     * AI 응답으로 받은 AiDeadlineResponseV1 에서 finalDeadline 을 로그에 남김
     */
    @Async
    public void sendDeadlineRequest(HubRouteAfterCommandV1 message) {

        log.info("[AI 비동기 호출 시작] orderId={}", message.orderId());

        AiDeadlineRequestV1 request = new AiDeadlineRequestV1(
            message.orderId(),
            message.startHubId(),
            message.startHubName(),
            message.startHubFullAddress(),
            message.endHubId(),
            message.endHubName(),
            message.endHubFullAddress(),
            message.receiverFirmId(),
            message.receiverFirmFullAddress(),
            message.receiverFirmOwnerName(),
            message.requestNote(),
            message.productName(),
            message.productQuantity(),
            message.orderCreatedAt(),
            message.expectedDeliveryDuration()
            // TODO: deliveryPersonName 추가 필요
        );

        AiDeadlineResponseV1 response = aiClient.generateDeadlineMessage(request);

        log.info("[AI 결과] orderId={}, finalDeadline={}", message.orderId(),response.finalDeadline());

        // TODO: Discord 서비스로 response.discordMessage() 전달
    }
}
