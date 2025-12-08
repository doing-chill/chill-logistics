package chill_logistics.order_server.infrastructure.product;

import chill_logistics.order_server.infrastructure.config.FeignConfig;
import chill_logistics.order_server.infrastructure.product.dto.ProductDecreaseRequestV1;
import chill_logistics.order_server.infrastructure.product.dto.ProductResponseV1;
import lib.web.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "product-server", configuration = FeignConfig.class)
public interface ProductFeignClient {

    @GetMapping("/v1/products/internal/{id}")
    BaseResponse<ProductResponseV1> readProductInternal(@PathVariable UUID id);

    @PatchMapping("/v1/products/internal/{id}/decrease")
    BaseResponse<Void> decreaseStockInternal(
            @PathVariable UUID id,
            @RequestBody ProductDecreaseRequestV1 request);
}
