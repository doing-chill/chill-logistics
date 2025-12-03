package chill_logistics.product_server.application.server;

import chill_logistics.product_server.application.command.SearchProductCommandV1;
import chill_logistics.product_server.application.dto.query.SearchProductSummaryResultV1;
import chill_logistics.product_server.domain.entity.Product;
import chill_logistics.product_server.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository productRepository;

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
}
