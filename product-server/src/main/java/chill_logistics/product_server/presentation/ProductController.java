package chill_logistics.product_server.presentation;

import chill_logistics.product_server.application.ProductFacade;
import chill_logistics.product_server.application.command.CreateProductCommandV1;
import chill_logistics.product_server.application.command.UpdateProductCommandV1;
import chill_logistics.product_server.presentation.dto.request.CreateProductRequestV1;
import chill_logistics.product_server.presentation.dto.request.UpdateProductRequestV1;
import chill_logistics.product_server.presentation.dto.response.CreateProductResponseV1;
import jakarta.validation.Valid;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductFacade productFacade;

    /* 상품 추가 */
    @PostMapping()
    public BaseResponse<CreateProductResponseV1> createProduct(
            @RequestBody @Valid CreateProductRequestV1 request
    ) {

        CreateProductCommandV1 command = new CreateProductCommandV1(
                request.name(),
                request.firmId(),
                request.hubId(),
                request.stockQuantity(),
                request.price(),
                request.sellable() != null ? request.sellable() : true
        );

        CreateProductResponseV1 response = CreateProductResponseV1.from(productFacade.createProduct(command));

        return BaseResponse.ok(response, BaseStatus.CREATED);
    }

    /* 상품 정보 수정 */
    @PatchMapping("/{id}")
    public BaseResponse<Void> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateProductRequestV1 request
    ) {

        UpdateProductCommandV1 command = new UpdateProductCommandV1(
                id,
                request.name(),
                request.stockQuantity(),
                request.price(),
                request.sellable()
        );

        productFacade.updateProduct(command);

        return BaseResponse.ok(BaseStatus.OK);
    }
}