package chill_logistics.order_server.application.service;

import chill_logistics.order_server.application.dto.command.SupplierInfoV1;
import chill_logistics.order_server.application.dto.command.*;
import chill_logistics.order_server.domain.entity.Order;
import chill_logistics.order_server.domain.entity.OrderProduct;
import chill_logistics.order_server.domain.entity.OrderQuery;
import chill_logistics.order_server.domain.entity.OrderStatus;
import chill_logistics.order_server.domain.event.EventPublisher;
import chill_logistics.order_server.domain.port.HubPort;
import chill_logistics.order_server.domain.port.ProductPort;
import chill_logistics.order_server.domain.repository.OrderQueryRepository;
import chill_logistics.order_server.domain.repository.OrderRepository;
import chill_logistics.order_server.domain.port.FirmPort;
import chill_logistics.order_server.lib.error.ErrorCode;
import java.time.LocalDateTime;
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
public class OrderCommandService {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final ProductPort productPort;
    private final HubPort hubPort;
    private final FirmPort firmPort;
    private final EventPublisher eventPublisher;

    private Order readOrderOrThrow(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Transactional
    public CreateOrderResultV1 createOrder(CreateOrderCommandV1 command) {

        // 업체 조회
        FirmResultV1 supplierResult = firmPort.readFirmById(command.supplierFirmId(), "SUPPLIER");
        FirmResultV1 receiverResult = firmPort.readFirmById(command.receiverFirmId(), "RECEIVER");

        // 주문 상품 체크 및 재고 감소
        List<OrderProductInfoV1> orderProductInfoList =
                command.productList()
                        .stream()
                        .map(p -> {
                            // 상품 조회
                            ProductResultV1 product = productPort.readProductById(p.productId());

                            // 공급 업체 소속 상품인지 체크
                            if (!product.firmId().equals(command.supplierFirmId())) {
                                throw new BusinessException(ErrorCode.PRODUCT_NOT_FROM_FIRM);
                            }

                            // 상품 재고 체크
                            if (product.stockQuantity() < p.quantity()) {
                                throw new BusinessException(ErrorCode.OUT_OF_STOCK);
                            }

                            // 상품 재고 감소
                            productPort.decreaseStock(p.productId(), p.quantity());

                            return new OrderProductInfoV1(
                                    p.productId(),
                                    product.name(),
                                    product.price(),
                                    p.quantity()
                            );
                        })
                        .toList();

        // 주문 생성
        Order order = Order.create(
                command.supplierFirmId(),
                command.receiverFirmId(),
                command.requestNote(),
                orderProductInfoList
        );

        Order createOrder = orderRepository.save(order);

        // 주문 생성 시 주문 읽기 생성
        // TODO: 추후 주문 읽기 전략 수정예정 (임시: 대표 상품)
        OrderQuery orderQuery = OrderQuery.create(
                createOrder,
                SupplierInfoV1.from(supplierResult),
                ReceiverInfoV1.from(receiverResult)
        );

        orderQueryRepository.save(orderQuery);

        // Kafka 메시지 생성
        OrderAfterCreateV1 message = new OrderAfterCreateV1(
                createOrder.getId(),
                supplierResult.hubId(),
                receiverResult.hubId(),
                createOrder.getReceiverFirmId(),
                receiverResult.firmFullAddress(),
                receiverResult.firmOwnerName(),
                createOrder.getRequestNote(),
                // TODO: 추후 주문 읽기 전략 수정예정 (임시: 대표 상품)
                createOrder.getOrderProductList().get(0).getProductName(),
                createOrder.getOrderProductList().get(0).getQuantity(),
                createOrder.getCreatedAt()
        );

        // Kafka 메시지 발행
        eventPublisher.sendOrderAfterCreate(message);

        return CreateOrderResultV1.from(
                createOrder,
                FirmInfoV1.from(supplierResult),
                FirmInfoV1.from(receiverResult)
        );
    }

    @Transactional
    public void updateOrderStatus(UUID id, UpdateOrderStatusCommandV1 command) {

        // 주문 조회
        Order order = readOrderOrThrow(id);

        // 담당 허브 소속 주문인지 체크
        if (SecurityUtils.hasRole(Role.HUB_MANAGER)) {
            List<UUID> managingHubId = hubPort.readHubId(SecurityUtils.getCurrentUserId());
            UUID receiverHubId = firmPort.readHubId(order.getReceiverFirmId());

            if (!managingHubId.contains(receiverHubId)) {
                throw new BusinessException(ErrorCode.ORDER_NOT_IN_MANAGING_HUB);
            }
        }

        // 상태 변경
        order.updateStatus(command.status());

        // TODO: 주문 읽기 업데이트 (OrderStatus)
    }

    @Transactional
    public void deleteOrder(UUID id) {

        // 주문 조회
        Order order = readOrderOrThrow(id);

        // 담당 허브 소속 주문인지 체크
        if (SecurityUtils.hasRole(Role.HUB_MANAGER)) {
            List<UUID> managingHubId = hubPort.readHubId(SecurityUtils.getCurrentUserId());
            UUID receiverHubId = firmPort.readHubId(order.getReceiverFirmId());

            if (!managingHubId.contains(receiverHubId)) {
                throw new BusinessException(ErrorCode.ORDER_NOT_IN_MANAGING_HUB);
            }
        }

        // 본인 주문인지 체크
        if (SecurityUtils.hasRole(Role.FIRM_MANAGER)) {
            UUID currentUserId = SecurityUtils.getCurrentUserId();

            if (!currentUserId.equals(order.getCreatedBy())) {
                throw new BusinessException(ErrorCode.ORDER_NOT_CREATED_BY_USER);
            }
        }

        order.updateStatus(OrderStatus.CANCELED);
        order.delete(SecurityUtils.getCurrentUserId());

        // 재고 복원
        for (OrderProduct p : order.getOrderProductList()) {
            productPort.recoverStock(p.getProductId(), p.getQuantity());
        }

        // Kafka 메시지 발행
        OrderCanceledV1 message = new OrderCanceledV1(
            order.getId(),
            order.getOrderStatus(),  // CANCELED
            LocalDateTime.now()
        );

        eventPublisher.sendOrderCanceled(message);

        // TODO: 주문 읽기 업데이트 (OrderStatus, delete)
    }
}
