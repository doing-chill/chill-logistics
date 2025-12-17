package chill_logistics.delivery_server.application.service;

import chill_logistics.delivery_server.domain.entity.FirmDelivery;
import chill_logistics.delivery_server.domain.entity.HubDelivery;
import chill_logistics.delivery_server.domain.repository.FirmDeliveryRepository;
import chill_logistics.delivery_server.domain.repository.HubDeliveryRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCancellationService {

    private final HubDeliveryRepository hubDeliveryRepository;
    private final FirmDeliveryRepository firmDeliveryRepository;

    /* [주문 취소/실패 시 배송 취소 메서드]
     * orderId 기준 HubDelivery / FirmDelivery 취소 처리
     */
    @Transactional
    public void cancelDeliveriesByOrder(UUID orderId) {

        List<HubDelivery> hubDeliveryList = hubDeliveryRepository.findByOrderId(orderId);
        List<FirmDelivery> firmDeliveryList = firmDeliveryRepository.findByOrderId(orderId);

        if (hubDeliveryList.isEmpty() && firmDeliveryList.isEmpty()) {
            log.info("[주문 취소에 따른 배송 취소] 대상 배송이 없습니다. orderId={}", orderId);

            return;
        }

        for (HubDelivery hubDelivery : hubDeliveryList) {
            hubDelivery.cancelDueToOrder();
        }

        for (FirmDelivery firmDelivery : firmDeliveryList) {
            firmDelivery.cancelDueToOrder();
        }

        log.info("[주문 취소에 따른 배송 취소] 처리 완료. orderId={}", orderId);
    }
}
