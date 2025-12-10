package chill_logistics.product_server.presentation;

import chill_logistics.product_server.application.ProductFacade;
import chill_logistics.product_server.presentation.dto.request.CreateProductRequestV1;
import chill_logistics.product_server.presentation.dto.request.ReadProductRequestV1;
import chill_logistics.product_server.presentation.dto.request.UpdateProductRequestV1;
import chill_logistics.product_server.presentation.dto.response.CreateProductResponseV1;
import chill_logistics.product_server.presentation.dto.response.ReadProductDetailResponseV1;
import chill_logistics.product_server.presentation.dto.response.ReadProductSummaryResponseV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
@Tag(name = "1. 상품 관리", description = "상품 관리용 API")
public class ProductController {

    private final ProductFacade productFacade;

    /* 상품 추가 */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "상품 추가", description = "상품 추가 API입니다.")
    public BaseResponse<CreateProductResponseV1> createProduct(
            @RequestBody @Valid CreateProductRequestV1 request) {

        CreateProductResponseV1 response =
                CreateProductResponseV1.from(
                        productFacade.createProduct(
                                request.toCommand()
                        )
                );

        return BaseResponse.ok(response, BaseStatus.CREATED);
    }

    /* 상품 정보 수정 */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "상품 정보 수정", description = "상품 정보 수정 API입니다.")
    public BaseResponse<Void> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateProductRequestV1 request) {

        productFacade.updateProduct(id, request.toCommand());

        return BaseResponse.ok(BaseStatus.OK);
    }

    /* 상품 삭제 */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
    @Operation(summary = "상품 삭제", description = "상품 삭제 API입니다.")
    public BaseResponse<Void> deleteProduct(@PathVariable UUID id) {

        productFacade.deleteProduct(id);

        return BaseResponse.ok(BaseStatus.OK);
    }

    /* 상품 목록 조회 */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "상품 목록 조회", description = "상품 목록 조회 API입니다.")
    public BaseResponse<List<ReadProductSummaryResponseV1>> readProductList(
            @ModelAttribute ReadProductRequestV1 request) {

        List<ReadProductSummaryResponseV1> response =
                productFacade
                        .readProductList(request.toCommand())
                        .stream()
                        .map(ReadProductSummaryResponseV1::from)
                        .toList();

        return BaseResponse.ok(response, BaseStatus.OK);
    }

    /* 상품 단건 조회 */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "상품 단건 조회", description = "상품 단건 조회 API입니다.")
    public BaseResponse<ReadProductDetailResponseV1> readProduct(@PathVariable UUID id) {

        ReadProductDetailResponseV1 response =
                ReadProductDetailResponseV1.from(
                        productFacade.readProduct(id)
                );

        return BaseResponse.ok(response, BaseStatus.OK);
    }
}