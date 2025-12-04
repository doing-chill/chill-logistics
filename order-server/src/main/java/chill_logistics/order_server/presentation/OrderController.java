package chill_logistics.order_server.presentation;

import chill_logistics.order_server.application.OrderFacade;
import chill_logistics.order_server.presentation.dto.request.CreateOrderRequestV1;
import chill_logistics.order_server.presentation.dto.request.UpdateOrderStatusRequestV1;
import chill_logistics.order_server.presentation.dto.response.CreateOrderResponseV1;
import jakarta.validation.Valid;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade orderFacade;

    /* 주문 생성 */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<CreateOrderResponseV1> createOrder(
            @RequestBody @Valid CreateOrderRequestV1 request) {

        CreateOrderResponseV1 response =
                CreateOrderResponseV1.from(
                        orderFacade.createOrder(
                                request.toCommand()
                        )
                );

        return BaseResponse.ok(response, BaseStatus.OK);
    }

    /* 주문 상태 수정 */
    @PatchMapping()
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> updateOrderStatus(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateOrderStatusRequestV1 request) {

        orderFacade.updateOrderStatus(id, request.toCommand());

        return BaseResponse.ok(BaseStatus.OK);
    }
}
