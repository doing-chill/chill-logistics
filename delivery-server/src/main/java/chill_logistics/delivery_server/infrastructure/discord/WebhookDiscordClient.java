package chill_logistics.delivery_server.infrastructure.discord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookDiscordClient implements DiscordClient {

    private final RestClient restClient;

    @Value("${custom.discord.webhook-url}")
    private String webhookUrl;

    @Override
    public void sendDeadlineMessage(String content) {

        try {
            restClient.post()
                .uri(webhookUrl)
                .body(new DiscordWebhookPayload(content))
                .retrieve()
                .toBodilessEntity();

            log.info("[Discord 전송 성공] message={}", content);

        } catch (Exception e) {
            log.error("[Discord 전송 실패] message={}", content, e);
        }
    }

    // Discord Webhook 기본 payload 형태
    private record DiscordWebhookPayload(String content) {}
}
