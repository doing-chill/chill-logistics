package chill_logistics.order_server.application.service;

import chill_logistics.order_server.application.dto.command.FirmResultV1;
import chill_logistics.order_server.application.dto.query.FirmQueryResultV1;
import chill_logistics.order_server.application.dto.query.ReadOrderCommandV1;
import chill_logistics.order_server.application.dto.query.ReadOrderDetailResultV1;
import chill_logistics.order_server.application.dto.query.ReadOrderSummaryResultV1;
import chill_logistics.order_server.domain.entity.Order;
import chill_logistics.order_server.domain.port.FirmPort;
import chill_logistics.order_server.domain.repository.OrderRepository;
import chill_logistics.order_server.lib.error.ErrorCode;
import lib.entity.Role;
import lib.util.SecurityUtils;
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
public class OrderQueryService {

    private final OrderRepository orderRepository;
    private final FirmPort firmPort;

    @Transactional(readOnly = true)
    public List<ReadOrderSummaryResultV1> readOrderList(ReadOrderCommandV1 command) {

        List<Order> orderList = orderRepository.readOrderList(
                command.supplierFirmId(),
                command.receiverFirmId(),
                command.orderStatus()
        );

        // 본인 주문인지 체크
        if (SecurityUtils.hasRole(Role.FIRM_MANAGER)) {
            UUID currentUserId = SecurityUtils.getCurrentUserId();

            orderList = orderList.stream()
                    .filter(o -> currentUserId.equals(o.getCreatedBy()))
                    .toList();
        }

        return orderList
                .stream()
                .map(ReadOrderSummaryResultV1::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReadOrderDetailResultV1 readOrder(UUID id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        // 본인 주문인지 체크
        if (SecurityUtils.hasRole(Role.FIRM_MANAGER)) {
            UUID currentUserId = SecurityUtils.getCurrentUserId();

            if (!currentUserId.equals(order.getCreatedBy())) {
                throw new BusinessException(ErrorCode.ORDER_NOT_CREATED_BY_USER);
            }
        }

        // 업체 조회
        FirmResultV1 supplierResult = firmPort.readFirmById(order.getSupplierFirmId(), "SUPPLIER");
        FirmResultV1 receiverResult = firmPort.readFirmById(order.getReceiverFirmId(), "RECEIVER");

        return ReadOrderDetailResultV1.from(
                order,
                FirmQueryResultV1.from(supplierResult),
                FirmQueryResultV1.from(receiverResult)
        );
    }
}
