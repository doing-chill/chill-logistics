package chill_logistics.delivery_server.infrastructure.ai;

import chill_logistics.delivery_server.infrastructure.ai.dto.request.AiDeadlineRequestV1;
import chill_logistics.delivery_server.infrastructure.ai.dto.response.AiDeadlineResponseV1;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
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

        String orderCreatedAt = request.orderCreatedAt()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String requestNote = (request.requestNote() == null || request.requestNote().isBlank())
            ? "없음" : request.requestNote();

        // 경유지 문자열 조립 ("대전센터, 부산센터" or "없음")
        String viaHubs;
        List<String> viaHubsNames = request.viaHubsNames();

        if (viaHubsNames == null || viaHubsNames.isEmpty()) {
            viaHubs = "없음";
        } else {
            viaHubs = String.join(", ", viaHubsNames);
        }

        return OpenAiConstants.DISCORD_FORMATTED_DEADLINE_PROMPT.formatted(
            request.orderId(),                  // %s (주문 번호)
            request.receiverFirmOwnerName(),    // %s (주문자 정보)
            orderCreatedAt,                     // %s (주문 시각)
            request.productName(),              // %s (상품명)
            request.productQuantity(),          // %d (상품 수량)
            requestNote,                        // %s (요청 사항)
            request.supplierHubName(),          // %s (발송지)
            viaHubs,                            // %s (경유지)
            request.receiverFirmFullAddress(),  // %s (도착지 전체 주소)
            request.expectedDeliveryDuration(), // %d (예상 총 소요 시간)
            request.deliveryPersonName()        // %s (배송 담당자 이름)
        );
    }

    private String extractDeadline(String outputText) {

        return Arrays.stream(outputText.split("\n"))
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
