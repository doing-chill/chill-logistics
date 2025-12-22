package chill_logistics.order_server.infrastructure.kafka.outbox;

import chill_logistics.order_server.domain.entity.OrderOutboxEvent;
import chill_logistics.order_server.domain.entity.OrderOutboxStatus;
import chill_logistics.order_server.domain.event.OutboxEventProducer;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxProcessor {

    private static final int BATCH_SIZE = 200;                 // 한 번의 process 에 처리할 이벤트 건 수
    private static final long SEND_CONFIRM_TIMEOUT_MS = 3000;  // Kafka 전송 요청 보낸 뒤, 성공 응답 기다리는 최대 시간
    private static final long FIXED_DELAY_MS = 500;            // Processor 스케줄러 실행 주기

    private final OrderOutboxEventRepository orderOutboxEventRepository;
    private final OutboxEventProducer outboxEventProducer;

    @Scheduled(fixedDelay = FIXED_DELAY_MS)
    @Transactional
    public void publishPendingEvents() {

        // 상태가 PENDING인 이벤트만 조회 & createdAt 기준 오래된 이벤트부터 처리
        List<OrderOutboxEvent> eventList = orderOutboxEventRepository.findPendingEvents(
            OrderOutboxStatus.PENDING,
            PageRequest.of(0, BATCH_SIZE)
        );

        if (eventList.isEmpty()) {
            return;
        }

        int published = 0;
        int failed = 0;
        int skipped = 0;

        for (OrderOutboxEvent event : eventList) {

            // 이미 PUBLISHED 또는 FAILED면 스킵
            if (event.alreadyHandled()) {
                skipped++;
                continue;
            }

            // 재시도 횟수 초과, backoff 미충족이면 스킵
            if (!event.readyToRetry()) {
                skipped++;
                continue;
            }

            String key = event.getOrderId().toString();

            try {
                SendResult<String, String> result = outboxEventProducer
                    .publish(event.getEventType(), key, event.getPayload())
                    .get(SEND_CONFIRM_TIMEOUT_MS, TimeUnit.MILLISECONDS);

                event.markPublished();
                published++;

            } catch (Exception e) {
                event.markFailed();
                failed++;
            }
        }
    }
}
