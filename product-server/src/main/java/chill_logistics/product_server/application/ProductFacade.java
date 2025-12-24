package chill_logistics.product_server.application;

import chill_logistics.product_server.application.dto.command.*;
import chill_logistics.product_server.application.dto.query.ReadProductCommandV1;
import chill_logistics.product_server.application.dto.query.ReadProductDetailResultV1;
import chill_logistics.product_server.application.dto.query.ReadProductInternalResultV1;
import chill_logistics.product_server.application.dto.query.ReadProductSummaryResultV1;
import chill_logistics.product_server.application.service.ProductCommandService;
import chill_logistics.product_server.application.service.ProductQueryService;
import chill_logistics.product_server.lib.error.ErrorCode;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;
    private final RedissonClient redissonClient;

    public CreateProductResultV1 createProduct(CreateProductCommandV1 command) {
        return productCommandService.createProduct(command);
    }

    public void updateProduct(UUID id, UpdateProductCommandV1 command) {
        productCommandService.updateProduct(id, command);
    }

    public void deleteProduct(UUID id) {
        productCommandService.deleteProduct(id);
    }

    public List<ReadProductSummaryResultV1> readProductList(ReadProductCommandV1 command) {
        return productQueryService.readProductList(command);
    }

    public ReadProductDetailResultV1 readProduct(UUID id) {
        return productQueryService.readProduct(id);
    }

    public ReadProductInternalResultV1 readProductInternal(UUID id) {
        return productQueryService.readProductInternal(id);
    }

    public void decreaseStockInternal(UpdateStockDecreaseCommandV1 command) {
        productCommandService.decreaseStockInternal(command);
    }

    public void recoverStockInternal(UpdateStockRecoverCommandV1 command) {
        productCommandService.recoverStockInternal(command);
    }

    public void decreaseStockInternalV2(UUID orderId, UUID productId, int quantity) {

        RLock lock = redissonClient.getLock("lock:product:" + productId);
        boolean locked = false;

        try {
            locked = lock.tryLock(100, 5, TimeUnit.SECONDS);

            if (!locked) {
                throw new BusinessException(ErrorCode.STOCK_LOCK_FAILED);
            }

            productCommandService.decreaseStockInternalV3(productId, quantity, orderId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.LOCK_INTERRUPTED);

        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
