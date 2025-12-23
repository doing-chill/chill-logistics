package chill_logistics.order_server.infrastructure.kafka.outbox;

import chill_logistics.order_server.domain.entity.OrderOutboxEvent;
import chill_logistics.order_server.domain.entity.OrderOutboxStatus;
import chill_logistics.order_server.domain.repository.OrderOutboxEventRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxProcessor {

    private static final int BATCH_SIZE = 200;       // 한 번의 process 에 처리할 이벤트 건 수
    private static final long FIXED_DELAY_MS = 500;  // Processor 스케줄러 실행 주기

    private final OrderOutboxEventRepository outboxEventRepository;
    private final OutboxEventTransactionManager transactionManager;

    @Scheduled(fixedDelay = FIXED_DELAY_MS)
    public void publishPendingEvents() {

        // 상태가 PENDING인 이벤트만 조회 & createdAt 기준 오래된 이벤트부터 처리
        List<OrderOutboxEvent> eventList =
            outboxEventRepository.findPendingEvents(OrderOutboxStatus.PENDING, BATCH_SIZE);

        if (eventList.isEmpty()) {
            return;
        }

        int processed = 0;
        int skipped = 0;

        for (OrderOutboxEvent event : eventList) {

            // 이미 처리되었거나, 재시도 조건 충족 안 되면 스킵
            if (event.alreadyHandled() || !event.readyToRetry()) {
                skipped++;
                continue;
            }

            // 이벤트 1건 → REQUIRES_NEW 트랜잭션에서 처리
            transactionManager.publishEvent(event.getId());
            processed++;
        }

        log.info("[OUTBOX 처리 완료] 조회건수={} 처리건수={} 스킵건수={}",
            eventList.size(), processed, skipped);
    }
}
