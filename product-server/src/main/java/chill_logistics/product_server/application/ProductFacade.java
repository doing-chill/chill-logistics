package chill_logistics.product_server.application;

import chill_logistics.product_server.application.command.CreateProductCommandV1;
import chill_logistics.product_server.application.command.DeleteProductCommandV1;
import chill_logistics.product_server.application.command.SearchProductCommandV1;
import chill_logistics.product_server.application.command.UpdateProductCommandV1;
import chill_logistics.product_server.application.dto.command.CreateProductResultV1;
import chill_logistics.product_server.application.dto.query.SearchProductSummaryResultV1;
import chill_logistics.product_server.application.server.ProductCommandService;
import chill_logistics.product_server.application.server.ProductQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<SearchProductSummaryResultV1> searchProductList(SearchProductCommandV1 command) {
        return productQueryService.searchProductList(command);
    }
}
