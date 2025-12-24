package chill_logistics.product_server.application.service;

import chill_logistics.product_server.domain.entity.Product;
import chill_logistics.product_server.domain.repository.ProductRepository;
import chill_logistics.product_server.lib.error.ErrorCode;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockDbApplyService {

    private final ProductRepository productRepository;

    @Transactional
    public void apply(UUID productId, int netDecrease) {

        if (netDecrease == 0) return;

        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        if (netDecrease > 0) {
            if (product.getStockQuantity() < netDecrease) {
                // 여기 터지면 이미 Redis는 반영된 상태라, 설계상 "정산 불일치" 경고 케이스
                // 우선은 실패로 두고 processing에 남겨 재처리 or 수동 조정
                throw new BusinessException(ErrorCode.DB_STOCK_INCONSISTENT);
            }
            product.decreaseStock(netDecrease);
        } else {
            product.recoverStock(-netDecrease);
        }
    }
}
