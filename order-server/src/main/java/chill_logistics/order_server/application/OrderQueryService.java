package chill_logistics.order_server.application;

import chill_logistics.order_server.application.dto.query.FirmQueryResultV1;
import chill_logistics.order_server.application.dto.query.ReadOrderCommandV1;
import chill_logistics.order_server.application.dto.query.ReadOrderDetailResultV1;
import chill_logistics.order_server.application.dto.query.ReadOrderSummaryResultV1;
import chill_logistics.order_server.domain.entity.Order;
import chill_logistics.order_server.domain.repository.OrderRepository;
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
public class OrderQueryService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<ReadOrderSummaryResultV1> readOrderList(ReadOrderCommandV1 command) {

        List<Order> orderList = orderRepository.readOrderList(
                command.supplierFirmId(),
                command.receiverFirmId(),
                command.orderStatus()
        );

        return orderList
                .stream()
                .map(ReadOrderSummaryResultV1::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReadOrderDetailResultV1 readOrder(UUID id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        // TODO: 업체 정보 조회 (supplier, receiver)
        FirmQueryResultV1 supplierResult = null;
        FirmQueryResultV1 receiverResult = null;

        return ReadOrderDetailResultV1.from(order, supplierResult, receiverResult);
    }
}
