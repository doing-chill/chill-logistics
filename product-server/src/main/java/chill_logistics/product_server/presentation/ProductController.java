package chill_logistics.product_server.presentation;

import chill_logistics.product_server.application.ProductFacade;
import chill_logistics.product_server.application.dto.command.CreateProductCommandV1;
import chill_logistics.product_server.application.dto.command.DeleteProductCommandV1;
import chill_logistics.product_server.application.dto.query.ReadProductCommandV1;
import chill_logistics.product_server.application.dto.command.UpdateProductCommandV1;
import chill_logistics.product_server.presentation.dto.request.CreateProductRequestV1;
import chill_logistics.product_server.presentation.dto.request.ReadProductRequestV1;
import chill_logistics.product_server.presentation.dto.request.UpdateProductRequestV1;
import chill_logistics.product_server.presentation.dto.request.UpdateStockDecreaseRequestV1;
import chill_logistics.product_server.presentation.dto.response.CreateProductResponseV1;
import chill_logistics.product_server.presentation.dto.response.ReadProductDetailResponseV1;
import chill_logistics.product_server.presentation.dto.response.ReadProductInternalResponseV1;
import chill_logistics.product_server.presentation.dto.response.ReadProductSummaryResponseV1;
import jakarta.validation.Valid;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductFacade productFacade;

    /* 상품 추가 */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<CreateProductResponseV1> createProduct(
            @RequestBody @Valid CreateProductRequestV1 request) {

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
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateProductRequestV1 request) {

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

    /* 상품 삭제 */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> deleteProduct(@PathVariable UUID id) {

        DeleteProductCommandV1 command = new DeleteProductCommandV1(id);

        productFacade.deleteProduct(command);

        return BaseResponse.ok(BaseStatus.OK);
    }

    /* 상품 목록 조회 */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<List<ReadProductSummaryResponseV1>> readProductList(
            @ModelAttribute ReadProductRequestV1 request) {

        ReadProductCommandV1 command = new ReadProductCommandV1(
                request.name(),
                request.firmId(),
                request.hubId(),
                request.sellable()
        );

        List<ReadProductSummaryResponseV1> response =
                productFacade
                        .readProductList(command)
                        .stream()
                        .map(ReadProductSummaryResponseV1::from)
                        .toList();

        return BaseResponse.ok(response, BaseStatus.OK);
    }

    /* 상품 단건 조회 */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<ReadProductDetailResponseV1> readProduct(@PathVariable UUID id) {

        ReadProductDetailResponseV1 response = ReadProductDetailResponseV1.from(productFacade.readProduct(id));

        return BaseResponse.ok(response, BaseStatus.OK);
    }

    /* 상품 단건 조회 (내부 API) */
    @GetMapping("/internal/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<ReadProductInternalResponseV1> readProductInternal(@PathVariable UUID id) {

        ReadProductInternalResponseV1 response = ReadProductInternalResponseV1.from(productFacade.readProductInternal(id));

        return BaseResponse.ok(response, BaseStatus.OK);
    }

    /* 재고 차감 (내부 API) */
    @PutMapping("/internal/{id}/decrease")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> decreaseStockInternal(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateStockDecreaseRequestV1 request) {

        productFacade.decreaseStockInternal(request.toCommand(id));

        return BaseResponse.ok(BaseStatus.OK);
    }

}