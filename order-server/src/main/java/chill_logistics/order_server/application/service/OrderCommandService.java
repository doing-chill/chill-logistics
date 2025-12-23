package chill_logistics.order_server.application.service;

import chill_logistics.order_server.application.dto.command.*;
import chill_logistics.order_server.domain.entity.*;
import chill_logistics.order_server.domain.port.FirmPort;
import chill_logistics.order_server.domain.port.HubPort;
import chill_logistics.order_server.domain.port.ProductPort;
import chill_logistics.order_server.domain.repository.OrderOutboxEventRepository;
import chill_logistics.order_server.domain.repository.OrderQueryRepository;
import chill_logistics.order_server.domain.repository.OrderRepository;
import chill_logistics.order_server.lib.error.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lib.entity.Role;
import lib.util.SecurityUtils;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCommandService {

    private static final String EVENT_ORDER_AFTER_CREATE = "OrderAfterCreateV1";
    private static final String EVENT_ORDER_CANCELED = "OrderCanceledV1";
    private static final String EVENT_STOCK_DECREASE = "StockDecreaseV1";

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final ProductPort productPort;
    private final HubPort hubPort;
    private final FirmPort firmPort;
    private final OrderOutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    private Order readOrderOrThrow(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    }

    // Outbox 이벤트 적재
    private void saveOutboxEvent(UUID orderId, String eventType, Object message) {

        final String payload;
        try {
            payload = objectMapper.writeValueAsString(message);

        } catch (JsonProcessingException e) {
            // Outbox payload 직렬화 실패는 주문 트랜잭션 자체를 실패시키는 게 일반적으로 안전
            log.error("[OUTBOX payload 직렬화 실패] orderId={} eventType={}", orderId, eventType, e);

            throw new BusinessException(ErrorCode.OUTBOX_PAYLOAD_SERIALIZATION_FAILED);
        }

        OrderOutboxEvent outboxEvent = OrderOutboxEvent.create(orderId, eventType, payload);

        outboxEventRepository.save(outboxEvent);

        log.info("[OUTBOX 이벤트 적재 완료] orderId={} eventType={} outboxId={}",
            orderId, eventType, outboxEvent.getId());
    }

    @Transactional
    public CreateOrderResultV1 createOrder(CreateOrderCommandV1 command) {

        // 업체 조회
        FirmResultV1 supplierResult = firmPort.readFirmById(command.supplierFirmId(), "SUPPLIER");
        FirmResultV1 receiverResult = firmPort.readFirmById(command.receiverFirmId(), "RECEIVER");

        // TODO: 재고 감소 비동기로 변경 필요 (카프카 이벤트 발행)
        // 주문 상품 정보 구성
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

        // 재고 확정 전 상태
        order.updateStatus(OrderStatus.STOCK_PROCESSING);

        Order createOrder = orderRepository.save(order);

        // 주문 생성 시 주문 읽기 생성
        // TODO: 추후 주문 읽기 전략 수정예정 (임시: 대표 상품)
        OrderQuery orderQuery = OrderQuery.create(
                createOrder,
                SupplierInfoV1.from(supplierResult),
                ReceiverInfoV1.from(receiverResult)
        );

        orderQueryRepository.save(orderQuery);

        // Kafka 메시지 생성 (즉시 발행하지 않음)
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

        // Outbox 적재
        saveOutboxEvent(createOrder.getId(), EVENT_ORDER_AFTER_CREATE, message);

        for (OrderProductInfoV1 p : orderProductInfoList) {

            StockDecreaseV1 stockDecreaseMessage = new StockDecreaseV1(
                    createOrder.getId(),
                    p.productId(),
                    p.quantity()
            );

            saveOutboxEvent(createOrder.getId(), EVENT_STOCK_DECREASE, stockDecreaseMessage);
        }

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

        // Kafka 메시지 생성 (즉시 발행하지 않음)
        OrderCanceledV1 message = new OrderCanceledV1(
            order.getId(),
            order.getOrderStatus(),  // CANCELED
            LocalDateTime.now()
        );

        saveOutboxEvent(order.getId(), EVENT_ORDER_CANCELED, message);

        // TODO: 주문 읽기 업데이트 (OrderStatus, delete)
    }

    @Transactional
    public void decreaseStockCompleted(UUID orderId, UUID productId) {

        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        order.markOrderProductStockConfirmed(productId);

        if (order.hasAnyStockFailed()) {
            order.cancelDueToStockFailure();
            return;
        }

        if (order.isAllOrderProductsStockConfirmed()) {
            order.markStockConfirmed();
        }
    }
}
