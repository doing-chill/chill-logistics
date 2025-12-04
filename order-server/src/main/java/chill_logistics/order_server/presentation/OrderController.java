package chill_logistics.order_server.presentation;

import chill_logistics.order_server.application.OrderFacade;
import chill_logistics.order_server.presentation.dto.request.CreateOrderRequestV1;
import chill_logistics.order_server.presentation.dto.response.CreateOrderResponseV1;
import jakarta.validation.Valid;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
