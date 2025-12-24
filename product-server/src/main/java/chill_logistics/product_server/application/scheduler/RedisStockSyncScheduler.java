package chill_logistics.product_server.application.scheduler;

import chill_logistics.product_server.application.service.StockSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisStockSyncScheduler {

    private final StockSyncService stockSyncService;

    // 30분마다 (초/분/시)
    @Scheduled(cron = "0 */30 * * * *")
    public void syncStockToDb() {
        stockSyncService.syncAllProducts();
    }
}