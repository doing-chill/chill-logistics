package chill_logistics.order_server.infrastructure.kafka.outbox;

import chill_logistics.order_server.domain.entity.OrderOutboxEvent;
import chill_logistics.order_server.domain.event.OutboxEventProducer;
import chill_logistics.order_server.domain.repository.OrderOutboxEventRepository;
import chill_logistics.order_server.lib.error.ErrorCode;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventTransactionManager {

    private static final long SEND_CONFIRM_TIMEOUT_MS = 3000;  // Kafka 전송 요청 보낸 뒤, 성공 응답 기다리는 최대 시간

    private final OrderOutboxEventRepository outboxEventRepository;
    private final OutboxEventProducer outboxEventProducer;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishEvent(UUID outboxId) {

        OrderOutboxEvent event = outboxEventRepository.findById(outboxId)
            .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_OUTBOX_EVENT_NOT_FOUND));

        // 이미 처리되었거나, 재시도 조건 충족 안 되면 종료
        if (event.alreadyHandled() || !event.readyToRetry()) {
            return;
        }

        String key = event.getOrderId().toString();

        try {
            // Kafka 전송 + 성공 확정 대기
            outboxEventProducer
                .publish(event.getEventType(), key, event.getPayload())
                .get(SEND_CONFIRM_TIMEOUT_MS, TimeUnit.MILLISECONDS);

            // 전송 성공 처리
            event.markPublished();

            log.info("[OUTBOX 이벤트 발행 성공] outboxId={} eventType={} orderId={}",
                event.getId(), event.getEventType(), event.getOrderId());

        } catch (Exception ex) {
            // 전송 실패 처리 (retryCount 증가)
            event.markFailed();

            log.warn("[OUTBOX 이벤트 발행 실패] outboxId={} 재시도 횟수={}",
                event.getId(), event.getRetryCount(), ex);
        }
    }
}
