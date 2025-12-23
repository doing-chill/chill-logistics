package chill_logistics.product_server.presentation;

import chill_logistics.product_server.application.ProductFacade;
import chill_logistics.product_server.presentation.dto.request.UpdateStockDecreaseRequestV1;
import chill_logistics.product_server.presentation.dto.request.UpdateStockRecoverRequestV1;
import chill_logistics.product_server.presentation.dto.response.ReadProductInternalResponseV1;
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

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
@Tag(name = "2. Internal - 상품 관리", description = "상품 관리용 내부 API")
public class ProductInternalController {

    private final ProductFacade productFacade;

    /* 상품 단건 조회 (내부 API) */
    @GetMapping("/internal/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'FIRM_MANAGER')")
    @Operation(summary = "상품 단건 조회 (내부 API)", description = "상품 단건 조회 (내부 API) API입니다.")
    public BaseResponse<ReadProductInternalResponseV1> readProductInternal(@PathVariable UUID id) {

        ReadProductInternalResponseV1 response =
                ReadProductInternalResponseV1.from(
                        productFacade.readProductInternal(id)
                );

        return BaseResponse.ok(response, BaseStatus.OK);
    }

    /* 재고 차감 (내부 API) */
    @PutMapping("/internal/{id}/decrease")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'FIRM_MANAGER')")
    @Operation(summary = "재고 차감 (내부 API)", description = "재고 차감 (내부 API) API입니다.")
    public BaseResponse<Void> decreaseStockInternal(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateStockDecreaseRequestV1 request) {

        productFacade.decreaseStockInternal(request.toCommand(id));

        return BaseResponse.ok(BaseStatus.OK);
    }

    /* 재고 복원 (내부 API) */
    @PutMapping("/internal/{id}/recover")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "재고 복원 (내부 API)", description = "재고 복원 (내부 API) API입니다.")
    public BaseResponse<Void> recoveryStockInternal(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateStockRecoverRequestV1 request) {

        productFacade.recoverStockInternal(request.toCommand(id));

        return BaseResponse.ok(BaseStatus.OK);
    }

    /* 재고 차감 - 검증 확인을 상품서버에서 처리하는 버전 (내부 API) */
    @PutMapping("/internal/{id}/decrease/{quantity}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'FIRM_MANAGER')")
    @Operation(summary = "재고 차감 (내부 API)", description = "재고 차감 (내부 API) API입니다.")
    public BaseResponse<Void> decreaseStockInternalV2(
            @PathVariable UUID id,
            @PathVariable int quantity) {

        productFacade.decreaseStockInternalV2(id, quantity);

        return BaseResponse.ok(BaseStatus.OK);
    }
}
