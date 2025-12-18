package chill_logistics.order_server.presentation;

import chill_logistics.order_server.application.OrderFacade;
import chill_logistics.order_server.presentation.dto.request.CreateOrderRequestV1;
import chill_logistics.order_server.presentation.dto.request.ReadOrderRequestV1;
import chill_logistics.order_server.presentation.dto.request.UpdateOrderStatusRequestV1;
import chill_logistics.order_server.presentation.dto.response.CreateOrderResponseV1;
import chill_logistics.order_server.presentation.dto.response.ReadOrderDetailResponseV1;
import chill_logistics.order_server.presentation.dto.response.ReadOrderSummaryResponseV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@Tag(name = "1. 주문 관리", description = "주문 관리용 API")
public class OrderController {

    private final OrderFacade orderFacade;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('MASTER', 'FIRM_MANAGER')")
    @Operation(summary = "주문 생성", description = "주문 생성 API입니다. 주문 생성 시 kafka 메시지를 발행합니다.")
    public BaseResponse<CreateOrderResponseV1> createOrder(
            @RequestHeader(value = "Idempotency-Key", required = false) UUID key,
            @RequestBody @Valid CreateOrderRequestV1 request) {

        CreateOrderResponseV1 response =
                CreateOrderResponseV1.from(
                        orderFacade.createOrder(
                                key,
                                request.toCommand()
                        )
                );

        return BaseResponse.ok(response, BaseStatus.OK);
    }

    /* 주문 상태 수정 */
    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
    @Operation(summary = "주문 상태 수정", description = "주문 상태 수정 API입니다.")
    public BaseResponse<Void> updateOrderStatus(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateOrderStatusRequestV1 request) {

        orderFacade.updateOrderStatus(id, request.toCommand());

        return BaseResponse.ok(BaseStatus.OK);
    }

    /* 주문 취소 */
    @DeleteMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "주문 취소", description = "주문 취소 API입니다. 주문 상태를 CANCELED로 변경하고 소프트 딜리트를 수행합니다.")
    public BaseResponse<Void> deleteOrder(@PathVariable UUID id) {

        orderFacade.deleteOrder(id);

        return BaseResponse.ok(BaseStatus.OK);
    }

    /* 주문 목록 조회 */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'HUB_DELIVERY_MANAGER', 'FIRM_DELIVERY_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "주문 목록 조회 ", description = "주문 목록 조회 API입니다. 검색 조건: 공급업체id, 수령업체id, 주문 상태")
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
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'HUB_DELIVERY_MANAGER', 'FIRM_DELIVERY_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "주문 단건 조회", description = "주문 단건 조회 API입니다.")
    public BaseResponse<ReadOrderDetailResponseV1> readOrder(@PathVariable UUID id) {

        ReadOrderDetailResponseV1 response =
                ReadOrderDetailResponseV1.from(orderFacade.readOrder(id));

        return BaseResponse.ok(response, BaseStatus.OK);
    }
}
