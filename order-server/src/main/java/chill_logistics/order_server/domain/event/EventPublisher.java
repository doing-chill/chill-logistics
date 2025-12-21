package chill_logistics.order_server.domain.event;

import chill_logistics.order_server.application.dto.command.OrderAfterCreateV1;
import chill_logistics.order_server.application.dto.command.OrderCanceledV1;

public interface EventPublisher {

    void sendOrderAfterCreate(OrderAfterCreateV1 message);

    void sendOrderCanceled(OrderCanceledV1 message);
}
