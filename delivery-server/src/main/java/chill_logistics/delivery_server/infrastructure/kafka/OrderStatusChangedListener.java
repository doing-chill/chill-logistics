package chill_logistics.delivery_server.infrastructure.kafka;

import chill_logistics.delivery_server.application.OrderStatus;
import chill_logistics.delivery_server.application.service.OrderCancellationService;
import chill_logistics.delivery_server.infrastructure.kafka.dto.OrderStatusChangedV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStatusChangedListener {

    private final OrderCancellationService orderCancellationService;

    @KafkaListener(
        topics = "order-status-changed",
        groupId = "delivery-server-order-status-group",
        containerFactory = "orderStatusChangedKafkaListenerContainerFactory"
    )
    public void listen(OrderStatusChangedV1 message) {

        log.info("Kafka 메시지 수신: {}", message);

        if (message.orderStatus() == OrderStatus.CANCELED) {
            orderCancellationService.cancelDeliveriesByOrder(
                message.orderId()
            );
        }
    }
}
