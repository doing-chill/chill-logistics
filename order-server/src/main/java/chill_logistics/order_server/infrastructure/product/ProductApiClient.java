package chill_logistics.order_server.infrastructure.product;

import chill_logistics.order_server.application.dto.command.ProductResultV1;
import chill_logistics.order_server.domain.port.ProductPort;
import chill_logistics.order_server.infrastructure.product.dto.ProductDecreaseRequestV1;
import chill_logistics.order_server.infrastructure.product.dto.ProductRecoverRequestV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductApiClient implements ProductPort {

    private final ProductFeignClient productFeignClient;

    @Override
    public ProductResultV1 readProductById(UUID productId) {
        return ProductResultV1.from(productFeignClient.readProductInternal(productId).getData());
    }

    @Override
    public void decreaseStock(UUID productId, int quantity) {
        ProductDecreaseRequestV1 request = new ProductDecreaseRequestV1(quantity);
        productFeignClient.decreaseStockInternal(productId, request);
    }

    @Override
    public void recoverStock(UUID productId, int quantity) {
        ProductRecoverRequestV1 request = new ProductRecoverRequestV1(quantity);
        productFeignClient.recoverStockInternal(productId, request);
    }
}
