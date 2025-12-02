package chill_logistics.product_server.presentation;

import chill_logistics.product_server.application.ProductFacade;
import chill_logistics.product_server.application.command.CreateProductCommandV1;
import chill_logistics.product_server.presentation.dto.request.CreateProductRequestV1;
import chill_logistics.product_server.presentation.dto.response.CreateProductResponseV1;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductFacade productFacade;

    /* 상품 추가 */
    @PostMapping()
    public BaseResponse<CreateProductResponseV1> createProduct(
            @RequestBody CreateProductRequestV1 request
    ) {

        CreateProductCommandV1 command = new CreateProductCommandV1(
                request.name(),
                request.firmId(),
                request.hubId(),
                request.stockQuantity(),
                request.price(),
                request.sellable()
        );

        CreateProductResponseV1 response = CreateProductResponseV1.from(productFacade.createProduct(command));

        return BaseResponse.ok(response, BaseStatus.CREATED);
    }

}
