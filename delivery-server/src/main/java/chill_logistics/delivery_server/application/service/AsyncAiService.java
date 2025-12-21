package chill_logistics.delivery_server.application.service;

import chill_logistics.delivery_server.application.dto.command.HubRouteAfterCommandV1;
import chill_logistics.delivery_server.application.dto.command.HubRouteHubInfoV1;
import chill_logistics.delivery_server.infrastructure.ai.AiClient;
import chill_logistics.delivery_server.infrastructure.ai.dto.request.AiDeadlineRequestV1;
import chill_logistics.delivery_server.infrastructure.ai.dto.response.AiDeadlineResponseV1;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncAiService {

    private final AiClient aiClient;
    private final AsyncDiscordService asyncDiscordService;

    /* [배송 최종 발송 시한 계산 요청]
     * HubRouteAfterCommandV1 메시지를 기반으로 AI 비동기 호출
     * HubRouteAfterCommandV1 → AiDeadlineRequestV1 로 변환 (AI에게 줄 데이터 정제)
     * pathHubs 기반 발송지/경유지/도착지 조립
     * AI 응답으로 받은 AiDeadlineResponseV1 → Discord 비동기 호출
     */
    @Async
    public void sendDeadlineRequest(HubRouteAfterCommandV1 message, String hubDeliveryPersonName) {

        log.info("[AI 비동기 호출 시작] orderId={}", message.orderId());

        List<HubRouteHubInfoV1> pathHubs = message.pathHubs();

        HubRouteHubInfoV1 startHub = pathHubs.get(0);

        // pathHubs 기반 경유지 목록 생성
        List<String> viaHubsNames = pathHubs.size() <= 2
            ? List.of("없음")
            : pathHubs.subList(1, pathHubs.size() - 1)
                .stream()
                .map(HubRouteHubInfoV1::hubName)
                .toList();

        AiDeadlineRequestV1 request = new AiDeadlineRequestV1(
            message.orderId(),
            message.receiverFirmOwnerName(),
            message.orderCreatedAt(),
            message.productName(),
            message.productQuantity(),
            message.requestNote(),
            startHub.hubName(),
            viaHubsNames,
            message.receiverFirmFullAddress(),
            hubDeliveryPersonName,
            message.expectedDeliveryDuration()
        );

        AiDeadlineResponseV1 response = aiClient.generateDeadlineMessage(request);

        log.info("[AI 결과] orderId={}, finalDeadline={}", message.orderId(),response.finalDeadline());

        asyncDiscordService.sendDeadlineMessage(request, response);
    }
}
