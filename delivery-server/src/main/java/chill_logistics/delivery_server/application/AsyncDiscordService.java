package chill_logistics.delivery_server.application;

import chill_logistics.delivery_server.infrastructure.ai.dto.request.AiDeadlineRequestV1;
import chill_logistics.delivery_server.infrastructure.ai.dto.response.AiDeadlineResponseV1;
import chill_logistics.delivery_server.infrastructure.discord.DiscordClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncDiscordService {

    private final DiscordClient discordClient;

    @Async
    public void sendDeadlineMessage(AiDeadlineRequestV1 request, AiDeadlineResponseV1 response) {

        String discordMessage = response.discordMessage();

        log.info("[Discord 비동기 호출 시작] orderId={}", request.orderId());

        discordClient.sendDeadlineMessage(discordMessage);

        log.info("[Discord 비동기 호출 완료] orderId={}", request.orderId());
    }
}
