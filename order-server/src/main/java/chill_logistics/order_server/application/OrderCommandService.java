package chill_logistics.order_server.application;

import chill_logistics.order_server.application.dto.ProductResultV1;
import chill_logistics.order_server.application.dto.command.*;
import chill_logistics.order_server.domain.entity.Order;
import chill_logistics.order_server.domain.entity.OrderProduct;
import chill_logistics.order_server.domain.entity.OrderQuery;
import chill_logistics.order_server.domain.entity.OrderStatus;
import chill_logistics.order_server.domain.event.EventPublisher;
import chill_logistics.order_server.domain.repository.OrderQueryRepository;
import chill_logistics.order_server.domain.repository.OrderRepository;
import chill_logistics.order_server.application.dto.command.OrderAfterCreateV1;
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
public class OrderCommandService {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final EventPublisher eventPublisher;

    private Order readProductOrThrow(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Transactional
    public CreateOrderResultV1 createOrder(CreateOrderCommandV1 command) {

        // TODO: 권한 체크

        // TODO: 업체 조회 후 업체 이름, hub id, (수령)업체 주소, (수령)업체 주인 이름 가져오기
        FirmResultV1 supplierResult = new FirmResultV1(command.supplierFirmId(), null, UUID.fromString("00000000-0000-0000-0000-000000000000"), null, null);
        FirmResultV1 receiverResult = new FirmResultV1(command.receiverFirmId(), null, UUID.fromString("00000000-0000-0000-0000-000000000000"), null, null);

        // TODO: 상품 조회 후 상품 정보(이름, 가격) 가져오기
        // TODO: 공급 업체 소속 삼품들인지 체크
        List<OrderProductInfoV1> orderProductInfoList =
                command.productList()
                        .stream()
                        .map(p -> {
                            // TODO: 상품 조회
                            ProductResultV1 product = null;

                            return new OrderProductInfoV1(
                                    p.productId(),
//                                    product.name(),
//                                    product.price(),
                                    "임시 상품",
                                    1234,
                                    p.quantity()
                            );
                        })
                        .toList();

        // TODO: 상품 재고 체크

        // 주문 생성
        Order order = Order.create(
                command.supplierFirmId(),
                command.receiverFirmId(),
                command.requestNote(),
                orderProductInfoList
        );

        Order createOrder = orderRepository.save(order);

        // 주문 생성 시 주문 읽기 생성
        // TODO: supplier, receiver 정보 수정 예정, info dto로 수정 예정
        // TODO: 추후 주문 읽기 전략 수정예정 (임시: 대표 상품)
        OrderQuery orderQuery = OrderQuery.from(
                createOrder,
                supplierResult,
                receiverResult
        );

        orderQueryRepository.save(orderQuery);

        // TODO: 주문 생성 시 order_after_create_message 발행
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

        // TODO: 권한 체크

        // 주문 조회
        Order order = readProductOrThrow(id);

        // 상태 변경
        order.updateStatus(command.status());

        // TODO: 주문 읽기 업데이트 (OrderStatus)
    }

    @Transactional
    public void deleteOrder(UUID id) {

        // TODO: 권한 체크

        // 주문 조회
        Order order = readProductOrThrow(id);

        order.updateStatus(OrderStatus.CANCELED);
        UUID userId = null;
        order.delete(userId);  // TODO: 로그인 유저id로 변경해야 함

        // TODO: 재고 복원 (상품 서버 api 호출 )
        List<OrderProduct> orderProductList= order.getOrderProductList();

        // TODO: 주문 읽기 업데이트 (OrderStatus, delete)
    }
}
