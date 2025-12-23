package chill_logistics.product_server.application.service;

import chill_logistics.product_server.domain.entity.Product;
import chill_logistics.product_server.domain.repository.ProductRepository;
import chill_logistics.product_server.infrastructure.kafka.StockDecreaseCompletedProducer;
import chill_logistics.product_server.infrastructure.kafka.dto.StockDecreaseCompletedV1;
import chill_logistics.product_server.lib.error.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockSyncService {

    private static final int BATCH_SIZE_PER_PRODUCT = 1000;

    private final StringRedisTemplate redisTemplate;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final StockDecreaseCompletedProducer stockDecreaseCompletedProducer;

    /**
     * 30분마다 호출되는 배치 엔트리포인트
     * - stock:log:* 키를 스캔해서 상품별로 처리
     */
    public void syncAllProducts() {

        ScanOptions options = ScanOptions.scanOptions()
                .match("stock:log:*")
                .count(500)
                .build();

        try (Cursor<byte[]> cursor = (Cursor<byte[]>) redisTemplate.getConnectionFactory()
                .getConnection()
                .scan(options)) {

            while (cursor.hasNext()) {
                String key = new String(cursor.next());

                // 처리중 키는 제외
                if (key.startsWith("stock:log:processing:")) continue;

                UUID productId = extractProductIdFromLogKey(key);
                syncOneProduct(productId);
            }

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.STOCK_SYNC_FAILED);
        }
    }

    private UUID extractProductIdFromLogKey(String logKey) {

        // logKey = "stock:log:{productId}"
        String[] parts = logKey.split(":");
        String uuid = parts[2]; // ["stock","log","{productId}"]

        return UUID.fromString(uuid);
    }

    /**
     * 상품 1개에 대해 로그를 일부(batch)만 처리.
     * - 원본 리스트 -> processing 리스트로 RPOPLPUSH로 옮기며 집계
     * - DB 반영 성공하면 processing에서 제거
     * - DB 반영 실패하면 processing에 남아 재처리 가능 (또는 원본으로 되돌리는 로직 추가 가능)
     */
    public void syncOneProduct(UUID productId) {

        String src = "stock:log:" + productId;
        String processing = "stock:log:processing:" + productId;

        int totalDecrease = 0;
        int moved = 0;

        Map<UUID, Integer> decreaseByOrder = new HashMap<>();

        // 1) 로그를 processing으로 옮기면서 합산
        for (int i = 0; i < BATCH_SIZE_PER_PRODUCT; i++) {
            String json = redisTemplate.opsForList().rightPopAndLeftPush(src, processing);
            if (json == null) break;

            moved++;

            UUID orderId = UUID.fromString(parseOrderId(json));
            int qty = parseQty(json);
            String type = parseType(json);

            int signedQty = "DECREASE".equals(type) ? qty : -qty;

            totalDecrease += signedQty;

            decreaseByOrder.merge(orderId, signedQty, Integer::sum);
        }

        if (moved == 0) return; // 처리할 로그 없음

        // 2) DB 반영 (트랜잭션)
        applyToDb(productId, totalDecrease);

        // 3) 주문 단위 완료 이벤트 생성
        for (Map.Entry<UUID, Integer> e : decreaseByOrder.entrySet()) {
            if (e.getValue() > 0) {
                StockDecreaseCompletedV1 message =
                        new StockDecreaseCompletedV1(
                                e.getKey(),
                                productId,
                                e.getValue(),
                                LocalDateTime.now()
                        );

                stockDecreaseCompletedProducer.sendStockDecreaseCompleted(message);
            }
        }

        // 4) 성공했으면 processing에서 moved개 제거 (우리는 leftPush 했으니 leftPop으로 제거)
        for (int i = 0; i < moved; i++) {
            redisTemplate.opsForList().leftPop(processing);
        }

        // (선택) processing이 비면 키 삭제
        Long remain = redisTemplate.opsForList().size(processing);
        if (remain != null && remain == 0L) {
            redisTemplate.delete(processing);
        }
    }

    private String parseOrderId(String json) {

        try {
            JsonNode n = objectMapper.readTree(json);
            return n.get(0).asText();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.STOCK_LOG_CORRUPTED);
        }
    }

    private int parseQty(String json) {

        try {
            JsonNode n = objectMapper.readTree(json);
            // Lua에서 log = {orderId, quantity, 'DECREASE', ts}
            return n.get(1).asInt();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.STOCK_LOG_CORRUPTED);
        }
    }

    private String parseType(String json) {

        try {
            JsonNode n = objectMapper.readTree(json);
            return n.get(2).asText();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.STOCK_LOG_CORRUPTED);
        }
    }

    // DB 반영
    @Transactional
    protected void applyToDb(UUID productId, int netDecrease) {

        if (netDecrease == 0) return;

        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        if (netDecrease > 0) {

            // 감소
            if (product.getStockQuantity() < netDecrease) {
                // 여기 터지면 이미 Redis는 반영된 상태라, 설계상 "정산 불일치" 경고 케이스
                // 우선은 실패로 두고 processing에 남겨 재처리 or 수동 조정
                throw new BusinessException(ErrorCode.DB_STOCK_INCONSISTENT);
            }

            product.decreaseStock(netDecrease);
        } else {
            // 증가(롤백이 더 많았던 경우)
            product.recoverStock(-netDecrease);
        }
    }
}
