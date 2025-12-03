package chill_logistics.product_server.application.server;

import chill_logistics.product_server.application.command.CreateProductCommandV1;
import chill_logistics.product_server.application.command.DeleteProductCommandV1;
import chill_logistics.product_server.application.command.UpdateProductCommandV1;
import chill_logistics.product_server.application.dto.command.CreateProductResultV1;
import chill_logistics.product_server.domain.entity.Product;
import chill_logistics.product_server.domain.repository.ProductRepository;
import chill_logistics.product_server.lib.exception.ErrorCode;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCommandService {

    private final ProductRepository productRepository;

    private Product readProductOrThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

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

        return CreateProductResultV1.from(createProduct);
    }

    @Transactional
    public void updateProduct(UpdateProductCommandV1 command) {

        Product product = readProductOrThrow(command.id());

        // 권한 체크

        // 허브 관리자면 관리 허브 소속 상품인지 체크

        // 업체 담당자면 본인 업체의 상품인지 체크

        product.update(
                command.name(),
                command.stockQuantity(),
                command.price(),
                command.sellable()
        );
    }

    @Transactional
    public void deleteProduct(DeleteProductCommandV1 command) {

        Product product = readProductOrThrow(command.id());

        // 임시 유저 id
        UUID userId = null;

        // 권한 체크

        // 허브 관리자면 관리 허브 소속 상품인지 체크

        product.delete(userId);
    }
}
