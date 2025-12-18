package chill_logistics.order_server.application;

import chill_logistics.order_server.application.dto.command.CreateOrderCommandV1;
import chill_logistics.order_server.application.dto.command.CreateOrderResultV1;
import chill_logistics.order_server.application.dto.command.UpdateOrderStatusCommandV1;
import chill_logistics.order_server.application.dto.query.ReadOrderCommandV1;
import chill_logistics.order_server.application.dto.query.ReadOrderDetailResultV1;
import chill_logistics.order_server.application.dto.query.ReadOrderSummaryResultV1;
import chill_logistics.order_server.application.service.OrderCommandService;
import chill_logistics.order_server.application.service.OrderIdempotencyService;
import chill_logistics.order_server.application.service.OrderQueryService;
import chill_logistics.order_server.lib.error.ErrorCode;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;
    private final OrderIdempotencyService orderIdempotencyService;

    @Transactional
    public CreateOrderResultV1 createOrder(UUID idempotencyKey, CreateOrderCommandV1 command) {

        // 멱등키로 완료된 요청인 지 확인
        // 아니라면 createNewOrder로 넘어감
        return orderIdempotencyService.findCompletedOrderId(idempotencyKey)
                .map(orderId ->
                        CreateOrderResultV1.fromReadOrder(
                                orderQueryService.readOrder(orderId)
                        )
                )
                .orElseGet(() -> createNewOrder(idempotencyKey, command));
    }

    private CreateOrderResultV1 createNewOrder(UUID idempotencyKey, CreateOrderCommandV1 command) {

        // 시작된 멱등키 값을 받아옴 (null 이면 새로 만들고, failed 이면 inProgress로 바꾸고 그 멱등키값)
        UUID startIdempotencyKey = orderIdempotencyService.start(idempotencyKey);

        try {
            CreateOrderResultV1 result = orderCommandService.createOrder(command);

            orderIdempotencyService.complete(startIdempotencyKey, result.id());
            return result;

        } catch (RuntimeException e) {
            orderIdempotencyService.fail(startIdempotencyKey);
            throw new BusinessException(ErrorCode.FAIlED_IDEMPOTENCY);
        }
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
