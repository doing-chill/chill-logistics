package chill_logistics.order_server.presentation;

import chill_logistics.order_server.application.OrderFacade;
import chill_logistics.order_server.presentation.dto.request.CreateOrderRequestV1;
import chill_logistics.order_server.presentation.dto.request.ReadOrderRequestV1;
import chill_logistics.order_server.presentation.dto.request.UpdateOrderStatusRequestV1;
import chill_logistics.order_server.presentation.dto.response.CreateOrderResponseV1;
import chill_logistics.order_server.presentation.dto.response.ReadOrderDetailResponseV1;
import chill_logistics.order_server.presentation.dto.response.ReadOrderSummaryResponseV1;
import jakarta.validation.Valid;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> updateOrderStatus(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateOrderStatusRequestV1 request) {

        orderFacade.updateOrderStatus(id, request.toCommand());

        return BaseResponse.ok(BaseStatus.OK);
    }

    /* 주문 취소 */
    @DeleteMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> deleteOrder(@PathVariable UUID id) {

        orderFacade.deleteOrder(id);

        return BaseResponse.ok(BaseStatus.OK);
    }

    /* 주문 목록 조회 */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<List<ReadOrderSummaryResponseV1>> readOrderList(
            @ModelAttribute ReadOrderRequestV1 request) {

        List<ReadOrderSummaryResponseV1> response =
                orderFacade.readOrderList(request.toCommand())
                        .stream()
                        .map(ReadOrderSummaryResponseV1::from)
                        .toList();

        return BaseResponse.ok(response, BaseStatus.OK);
    }

    /* 주문 단건 조회 */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<ReadOrderDetailResponseV1> readOrder(@PathVariable UUID id) {

        ReadOrderDetailResponseV1 response = ReadOrderDetailResponseV1.from(orderFacade.readOrder(id));

        return BaseResponse.ok(response, BaseStatus.OK);
    }
}
