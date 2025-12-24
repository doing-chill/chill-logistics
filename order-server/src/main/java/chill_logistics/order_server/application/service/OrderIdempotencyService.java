package chill_logistics.order_server.application.service;

import chill_logistics.order_server.lib.error.ErrorCode;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderIdempotencyService {

    private static final String IDEMPOTENCY_KEY_PREFIX = "order:idempotency:";

    private final StringRedisTemplate redisTemplate;

    // 완료된 주문 ID를 찾기 (멱등성 키 상태 확인)
    public Optional<UUID> findCompletedOrderId(UUID key) {

        String status = redisTemplate.opsForValue().get(IDEMPOTENCY_KEY_PREFIX + key);

        if ("COMPLETED".equals(status)) {
            String orderIdString = redisTemplate.opsForValue().get(IDEMPOTENCY_KEY_PREFIX + key + ":orderId");

            if (orderIdString == null) {
                throw new BusinessException(ErrorCode.ORDER_ID_NOT_FOUND);
            }

            return Optional.of(UUID.fromString(orderIdString));
        }

        return Optional.empty();
    }

    // 멱등성 키 상태 시작
    public UUID start(UUID key) {

        // key가 null이면 새로운 UUID를 생성
        if (key == null) {
            key = UUID.randomUUID();
            redisTemplate.opsForValue().set(IDEMPOTENCY_KEY_PREFIX + key, "IN_PROGRESS");
            return key;
        }

        String currentStatus = redisTemplate.opsForValue().get(IDEMPOTENCY_KEY_PREFIX + key);

        if (currentStatus != null) {
            if ("FAILED".equals(currentStatus)) {
                redisTemplate.opsForValue().set(IDEMPOTENCY_KEY_PREFIX + key, "IN_PROGRESS");
                return key;
            } else {
                throw new BusinessException(ErrorCode.IDEMPOTENCY_ALREADY_IN_PROGRESS);
            }
        } else {
            redisTemplate.opsForValue().set(IDEMPOTENCY_KEY_PREFIX + key, "IN_PROGRESS");
            return key;
        }
    }

    // 멱등성 키 처리 완료
    public void complete(UUID key, UUID orderId) {
        redisTemplate.opsForValue().set(IDEMPOTENCY_KEY_PREFIX + key, "COMPLETED");
        redisTemplate.opsForValue().set(IDEMPOTENCY_KEY_PREFIX + key + ":orderId", orderId.toString());
    }

    // 멱등성 키 실패 처리
    public void fail(UUID key) {
        redisTemplate.opsForValue().set(IDEMPOTENCY_KEY_PREFIX + key, "FAILED");
    }
}
