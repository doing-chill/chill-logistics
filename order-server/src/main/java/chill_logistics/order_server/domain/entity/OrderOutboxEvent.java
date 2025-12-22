package chill_logistics.order_server.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lib.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table(name = "p_order_outbox_event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class OrderOutboxEvent extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuidv7")
    @GenericGenerator(
        name = "uuidv7",
        strategy = "lib.id.UUIDv7Generator"
    )
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "order_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID orderId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_outbox_status", nullable = false, length = 30)
    private OrderOutboxStatus orderOutboxStatus;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "last_retry_at")
    private LocalDateTime lastRetryAt;

    private static final int MAX_RETRY_COUNT = 3;
    private static final long MAX_BACKOFF_MILLIS = 100L;

    private OrderOutboxEvent(UUID orderId, String eventType, String payload) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.payload = payload;
        this.orderOutboxStatus = OrderOutboxStatus.PENDING;
        this.retryCount = 0;
    }

    public static OrderOutboxEvent create(UUID orderId, String eventType, String payload) {
        return new OrderOutboxEvent(orderId, eventType, payload);
    }

    /* 이미 처리 되었는지 */
    public boolean alreadyHandled() {
        return this.orderOutboxStatus == OrderOutboxStatus.PUBLISHED
            || this.orderOutboxStatus == OrderOutboxStatus.FAILED;
    }

    /* 재시도 해도 되는지 */
    public boolean canRetry() {
        return this.orderOutboxStatus == OrderOutboxStatus.PENDING
            && this.retryCount < MAX_RETRY_COUNT;
    }

    /* 지금 재시도 할 준비 되었는지 */
    public boolean readyToRetry() {

        if (!canRetry()) {
            return false;
        }

        // 첫 시도는 즉시 성공
        if (this.lastRetryAt == null) {
            return true;
        }

        long timeSinceLastRetryMillis =
            Duration.between(this.lastRetryAt, LocalDateTime.now()).toMillis();

        return timeSinceLastRetryMillis >= MAX_BACKOFF_MILLIS;
    }

    /* 성공 확정 */
    public void markPublished() {
        this.orderOutboxStatus = OrderOutboxStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }

    /* 실패 처리 */
    public void markFailed() {
        this.retryCount++;
        this.lastRetryAt = LocalDateTime.now();

        // 최대 초과 시 FAILED
        if (this.retryCount >= MAX_RETRY_COUNT) {
            this.orderOutboxStatus = OrderOutboxStatus.FAILED;
        }
    }
}
