package chill_logistics.product_server.application;

import chill_logistics.product_server.application.dto.command.*;
import chill_logistics.product_server.application.dto.query.*;
import chill_logistics.product_server.application.service.ProductCommandService;
import chill_logistics.product_server.application.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    public CreateProductResultV1 createProduct(CreateProductCommandV1 command) {
        return productCommandService.createProduct(command);
    }

    public void updateProduct(UpdateProductCommandV1 command) {
        productCommandService.updateProduct(command);
    }

    public void deleteProduct(DeleteProductCommandV1 command) {
        productCommandService.deleteProduct(command);
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
}
