package chill_logistics.product_server.application.server;

import chill_logistics.product_server.application.command.CreateProductCommandV1;
import chill_logistics.product_server.application.dto.CreateProductResultV1;
import chill_logistics.product_server.domain.entity.Product;
import chill_logistics.product_server.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCommandService {

    private final ProductRepository productRepository;

    @Transactional
    public CreateProductResultV1 createProduct(CreateProductCommandV1 command) {

        // 상품 업체가 존재하는지 확인

        // 상품 관리 허브 ID를 확인하여 존재하는지 확인

        // 권한 체크

        // 상품 생성
        Product product = Product.create(
                command.name(),
                command.firmId(),
                command.hubId(),
                command.stockQuantity(),
                command.price(),
                command.sellable()
        );

        Product createProduct = productRepository.save(product);

        CreateProductResultV1.from(createProduct);

        return CreateProductResultV1.from(createProduct);
    }
}
