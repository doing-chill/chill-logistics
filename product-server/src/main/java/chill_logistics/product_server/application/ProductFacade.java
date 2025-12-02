package chill_logistics.product_server.application;

import chill_logistics.product_server.application.command.CreateProductCommandV1;
import chill_logistics.product_server.application.command.UpdateProductCommandV1;
import chill_logistics.product_server.application.dto.CreateProductResultV1;
import chill_logistics.product_server.application.server.ProductCommandService;
import chill_logistics.product_server.application.server.ProductQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
