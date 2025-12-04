package chill_logistics.order_server.application;

import chill_logistics.order_server.application.dto.command.CreateOrderCommandV1;
import chill_logistics.order_server.application.dto.command.CreateOrderResultV1;
import chill_logistics.order_server.application.dto.command.UpdateOrderStatusCommandV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    public CreateOrderResultV1 createOrder(CreateOrderCommandV1 command) {
        return orderCommandService.createOrder(command);
    }

    public void updateOrderStatus(UUID id, UpdateOrderStatusCommandV1 command) {
        orderCommandService.updateOrderStatus(id, command);
    }
}
