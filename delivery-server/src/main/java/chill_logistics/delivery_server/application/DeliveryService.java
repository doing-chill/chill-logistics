package chill_logistics.delivery_server.application;

import chill_logistics.delivery_server.domain.entity.DeliveryStatus;
import chill_logistics.delivery_server.domain.repository.FirmDeliveryRepository;
import chill_logistics.delivery_server.domain.repository.HubDeliveryRepository;
import chill_logistics.delivery_server.infrastructure.kafka.dto.OrderAfterCreateV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final HubDeliveryRepository hubDeliveryRepository;
    private final FirmDeliveryRepository firmDeliveryRepository;

    @Transactional
    public void createDelivery(OrderAfterCreateV1 message) {

        log.info("배송 생성 시작 - Kafka 메시지: {}", message);

        // Kafka 메시지의 String 상태값 → DeliveryStatus ENUM 변환
        DeliveryStatus deliveryStatus = DeliveryStatus.from(message.getDeliveryStatus());

        /* TODO
         * 배송 생성 로직 필요

         * Feign으로 Hub에서 받아올 것:
           * @NotBlank
             private String startHubName;
           * @NotBlank
             private String endHubName;
         */
    }
}
