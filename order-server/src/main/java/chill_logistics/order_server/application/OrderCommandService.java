package chill_logistics.order_server.application;

import chill_logistics.order_server.application.dto.ProductResultV1;
import chill_logistics.order_server.application.dto.command.*;
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
public class OrderCommandService {

    private final OrderRepository orderRepository;

    private Order readProductOrThrow(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Transactional
    public CreateOrderResultV1 createOrder(CreateOrderCommandV1 command) {

        // TODO: 권한 체크

        // TODO: 업체 조회 후 업체 이름, hub id, (수령)업체 주소, (수령)업체 주인 이름 가져오기
        FirmResultV1 supplierResult = null;
        FirmResultV1 receiverResult = null;

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
                                    product.name(),
                                    product.price(),
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

        // TODO: 주문 생성 시 order_after_create_message 발행
        // TODO: 주문 생성 시 주문 읽기도 생성

        return CreateOrderResultV1.from(createOrder, FirmInfoV1.from(supplierResult), FirmInfoV1.from(receiverResult));
    }

    @Transactional
    public void updateOrderStatus(UUID id, UpdateOrderStatusCommandV1 command) {

        // TODO: 권한 체크

        // 주문 조회
        Order order = readProductOrThrow(id);

        // 상태 변경
        order.updateStatus(command.status());

        // TODO: 주문 읽기 업데이트
    }
}
