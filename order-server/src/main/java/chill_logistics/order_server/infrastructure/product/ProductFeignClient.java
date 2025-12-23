package chill_logistics.order_server.infrastructure.product;

import chill_logistics.order_server.infrastructure.config.FeignConfig;
import chill_logistics.order_server.infrastructure.product.dto.ProductRecoverRequestV1;
import chill_logistics.order_server.infrastructure.product.dto.ProductResponseV1;
import lib.web.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient(name = "product-server", configuration = FeignConfig.class)
public interface ProductFeignClient {

    @GetMapping("/v1/products/internal/{id}")
    BaseResponse<ProductResponseV1> readProductInternal(@PathVariable UUID id);

    @PutMapping("/v1/products/internal/{id}/recover")
    BaseResponse<Void> recoverStockInternal(@PathVariable UUID id, ProductRecoverRequestV1 request);

    @PutMapping("/v1/products/internal/{id}/decrease/{quantity}")
    BaseResponse<Void> decreaseStockInternal(
            @PathVariable UUID id,
            @PathVariable int quantity);

}
