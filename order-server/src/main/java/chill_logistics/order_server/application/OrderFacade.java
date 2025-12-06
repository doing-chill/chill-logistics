package chill_logistics.order_server.application;

import chill_logistics.order_server.application.dto.command.CreateOrderCommandV1;
import chill_logistics.order_server.application.dto.command.CreateOrderResultV1;
import chill_logistics.order_server.application.dto.command.UpdateOrderStatusCommandV1;
import chill_logistics.order_server.application.dto.query.ReadOrderCommandV1;
import chill_logistics.order_server.application.dto.query.ReadOrderDetailResultV1;
import chill_logistics.order_server.application.dto.query.ReadOrderSummaryResultV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void deleteOrder(UUID id) {
        orderCommandService.deleteOrder(id);
    }

    public List<ReadOrderSummaryResultV1> readOrderList(ReadOrderCommandV1 command) {
        return orderQueryService.readOrderList(command);
    }

    public ReadOrderDetailResultV1 readOrder(UUID id) {
        return orderQueryService.readOrder(id);
    }
}
