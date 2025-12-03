package chill_logistics.product_server.application.server;

import chill_logistics.product_server.application.command.SearchProductCommandV1;
import chill_logistics.product_server.application.dto.query.ReadProductDetailResultV1;
import chill_logistics.product_server.application.dto.query.SearchProductSummaryResultV1;
import chill_logistics.product_server.domain.entity.Product;
import chill_logistics.product_server.domain.repository.ProductRepository;
import chill_logistics.product_server.lib.exception.ErrorCode;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<SearchProductSummaryResultV1> searchProductList(SearchProductCommandV1 command) {

        // 권한 체크

        List<Product> productList = productRepository.searchProductList(
                command.name(),
                command.firmId(),
                command.hubId(),
                command.sellable()
        );

        return productList
                .stream()
                .map(SearchProductSummaryResultV1::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReadProductDetailResultV1 readProduct(UUID id) {

        // 권한 체크

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        return ReadProductDetailResultV1.from(product);
    }
}
