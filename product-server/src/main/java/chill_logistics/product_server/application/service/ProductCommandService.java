package chill_logistics.product_server.application.service;

import chill_logistics.product_server.application.dto.command.*;
import chill_logistics.product_server.application.dto.query.ReadProductInternalResultV1;
import chill_logistics.product_server.domain.entity.Product;
import chill_logistics.product_server.domain.port.FirmPort;
import chill_logistics.product_server.domain.repository.ProductRepository;
import chill_logistics.product_server.lib.error.ErrorCode;
import lib.util.SecurityUtils;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCommandService {

    private final ProductRepository productRepository;
    private final FirmPort firmPort;
    private final StringRedisTemplate redisTemplate;

    private Product readProductOrThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Transactional
    public CreateProductResultV1 createProduct(CreateProductCommandV1 command) {

        // 상품 업체가 존재하는지 확인
        firmPort.validateExists(command.firmId());

        // 상품 관리 허브 ID를 확인하여 존재하는지 확인

        // 상품 생성
        Product product = Product.create(
                command.name(),
                command.firmId(),
                command.hubId(),
                command.stockQuantity(),
                command.price(),
                command.sellable()
        );

        Product createProduct = productRepository.save(product);

        return CreateProductResultV1.from(createProduct);
    }

    @Transactional
    public void updateProduct(UUID id, UpdateProductCommandV1 command) {

        Product product = readProductOrThrow(id);

        // 허브 관리자면 관리 허브 소속 상품인지 체크

        // 업체 담당자면 본인 업체의 상품인지 체크

        product.update(
                command.name(),
                command.stockQuantity(),
                command.price(),
                command.sellable()
        );
    }

    @Transactional
    public void deleteProduct(UUID id) {

        Product product = readProductOrThrow(id);

        UUID currentUserId = SecurityUtils.getCurrentUserId();

        // 권한 체크

        // 허브 관리자면 관리 허브 소속 상품인지 체크

        product.delete(currentUserId);
    }

    @Transactional
    public void decreaseStockInternal(UpdateStockDecreaseCommandV1 command) {

        Product product = readProductOrThrow(command.id());

        product.decreaseStock(command.quantity());
    }

    @Transactional
    public void recoverStockInternal(UpdateStockRecoverCommandV1 command) {

        Product product = readProductOrThrow(command.id());

        product.recoverStock(command.quantity());
    }

    // 비관적 락
    @Transactional
    public ReadProductInternalResultV1 decreaseStockInternalV2(UUID id, int quantity) {

        // 비관적 락 o
//        Product product = productRepository.findByIdForUpdate(id)
//                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        // 비관적 락 x
        Product product = readProductOrThrow(id);

        // 여기서부터는 row lock이 걸린 상태
        if (product.getStockQuantity() < quantity) {
            throw new BusinessException(ErrorCode.OUT_OF_STOCK);
        }

        product.decreaseStock(quantity); // 엔티티 상태 변경
        // flush 시점에 UPDATE 발생

        return ReadProductInternalResultV1.from(product);
    }

    // 분산 락
    public void decreaseStockInternalV3(UUID productId, int quantity, UUID orderId) {

        // Lua 스크립트 작성 (Redis에서 재고 차감 및 차감 로그 기록)
        String luaScript =
                "local stockKey = KEYS[1]; " +  // stock key (Redis 키)
                "local logKey = KEYS[2]; " +    // log key (Redis 로그 키)
                "local quantity = tonumber(ARGV[1]); " + // 차감할 수량
                "local orderId = ARGV[2]; " +  // 주문 ID

                // 1. 재고 조회
                "local currentStock = tonumber(redis.call('GET', stockKey)); " +
                "if currentStock == nil then return redis.error_reply('REDIS_STOCK_NOT_INITIALIZED'); end; " +
                "if currentStock < quantity then return redis.error_reply('OUT_OF_STOCK'); end; " +

                // 2. 재고 차감
               "redis.call('DECRBY', stockKey, quantity); " +

               // 3. 차감 로그 기록
               "local log = {orderId, quantity, 'DECREASE', tonumber(redis.call('TIME')[1])}; " +
               "redis.call('LPUSH', logKey, cjson.encode(log)); " +

               "return 1;"; // 성공 시 1 반환

        // Redis Lua 스크립트 실행
        List<String> keys = Arrays.asList("stock:" + productId, "stock:log:" + productId); // Redis 키들
        List<String> args = Arrays.asList(String.valueOf(quantity), orderId.toString()); // 인수들

        try {
            // Lua 스크립트 실행
            Long result = redisTemplate.execute(
                    new DefaultRedisScript<>(luaScript, Long.class), keys, args.toArray());

            if (result == null) {
                throw new BusinessException(ErrorCode.REDIS_OPERATION_FAILED); // Redis 실패
            }

        } catch (Exception e) {
            // Lua 스크립트에서 반환된 에러 메시지에 따른 예외 처리
            if ("OUT_OF_STOCK".equals(e.getMessage())) {
                // 재고 부족
                throw new BusinessException(ErrorCode.OUT_OF_STOCK);
            } else if ("REDIS_STOCK_NOT_INITIALIZED".equals(e.getMessage())) {
                // Redis가 초기화되지 않은 경우
                throw new BusinessException(ErrorCode.REDIS_STOCK_NOT_INITIALIZED);
            } else {
                // 그 외의 예외는 다시 던짐
                throw new BusinessException(ErrorCode.REDIS_OPERATION_FAILED);
            }
        }
    }
}
