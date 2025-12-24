package chill_logistics.product_server.application.service;

import chill_logistics.product_server.domain.entity.Product;
import chill_logistics.product_server.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductStockRedisInitializer {

    private final ProductRepository productRepository;
    private final StringRedisTemplate redisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void loadStockToRedis() {

        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            String key = "stock:" + product.getId();
            redisTemplate.opsForValue()
                    .set(key, String.valueOf(product.getStockQuantity()));
        }

        log.info("Redis 재고 초기화: {} products", products.size());
    }
}
