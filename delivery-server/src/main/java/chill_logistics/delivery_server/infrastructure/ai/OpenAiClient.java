package chill_logistics.delivery_server.infrastructure.ai;

import chill_logistics.delivery_server.infrastructure.ai.dto.request.AiDeadlineRequestV1;
import chill_logistics.delivery_server.infrastructure.ai.dto.response.AiDeadlineResponseV1;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiClient implements AiClient {

    private final ChatClient chatClient;

    @Override
    public AiDeadlineResponseV1 generateDeadlineMessage(AiDeadlineRequestV1 request) {

        String prompt = buildPrompt(request);

        log.info("[AI 요청] 배송 최종 발송 시한 계산, orderId={}", request.orderId());

        String outputText = chatClient
            .prompt()
            .user(prompt)
            .call()
            .content();

        log.info("[AI 응답 수신] 배송 최종 발송 시한 계산 완료, orderId={}", request.orderId());

        String finalDeadline = extractDeadline(outputText);

        return new AiDeadlineResponseV1(outputText, finalDeadline);
    }

    private String buildPrompt(AiDeadlineRequestV1 request) {

        String requestNote = (request.requestNote() == null || request.requestNote().isBlank())
            ? "없음" : request.requestNote();

        // 아직 허브 경유지 리스트가 따로 없으니 일단 "없음" 처리
        // TODO: 허브 경로 문자열을 전달받으면 실제 경유지 정보로 교체
        String route = "없음";

        return OpenAiConstants.DISCORD_FORMATTED_DEADLINE_PROMPT.formatted(
            request.orderId(),
            request.receiverFirmOwnerName(),    // %s
            request.orderCreatedAt(),           // %s
            request.productName(),              // %s
            request.productQuantity(),          // %d
            requestNote,                        // %s
            request.supplierHubName(),          // %s (발송지)
            route,                              // %s (경유지)
            request.receiverHubFullAddress(),   // %s (도착지 전체 주소)
            request.expectedDeliveryDuration()  // %d (예상 소요 시간)
            // TODO: deliveryPersonName 추가 필요
        );
    }

    private String extractDeadline(String deadline) {

        return Arrays.stream(deadline.split("\n"))
            .map(String::trim)
            .filter(line -> line.startsWith("위 내용을 기반으로 도출된 최종 발송 시한은"))
            .findFirst()
            .map(line -> line
                .replace("위 내용을 기반으로 도출된 최종 발송 시한은 ", "")
                .replace(" 입니다.", "")
                .trim())
            .orElse(null);
    }
}
