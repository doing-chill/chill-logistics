package chill_logistics.order_server.domain.event;

import chill_logistics.order_server.application.dto.command.OrderAfterCreateV1;

public interface EventPublisher {
    void sendOrderAfterCreate(OrderAfterCreateV1 message);
}
