package chill_logistics.order_server.infrastructure.product;

import chill_logistics.order_server.infrastructure.product.dto.ProductDecreaseRequestV1;
import chill_logistics.order_server.infrastructure.product.dto.ProductResponseV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(name = "product-server")
public interface ProductFeignClient {

    @GetMapping("/v1/products/internal/{id}")
    ProductResponseV1 readProductInternal(UUID productId);

    @PostMapping("/v1/products/internal/{id}/decrease")
    void decreaseStockInternal(UUID productId, ProductDecreaseRequestV1 request);
}
