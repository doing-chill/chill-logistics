package chill_logistics.order_server.application;

import chill_logistics.order_server.application.dto.ProductResultV1;
import chill_logistics.order_server.application.dto.command.*;
import chill_logistics.order_server.domain.entity.Order;
import chill_logistics.order_server.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCommandService {

    private final OrderRepository orderRepository;

    @Transactional
    public CreateOrderResultV1 createOrder(CreateOrderCommandV1 command) {

        // TODO: 권한 체크

        // TODO: 업체 조회 후 업체 이름, hub id 가져오기
        FirmResultV1 supplierResult = null;
        FirmResultV1 receiverResult = null;

        // TODO: 허브 조회 후 허브 주소 가져오기

        // TODO: 상품 조회 후 상품 정보(이름, 가격) 가져오기
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

        // 주문 생성
        Order order = Order.create(
                command.supplierFirmId(),
                command.receiverFirmId(),
                command.requestNote(),
                orderProductInfoList
        );

        Order createOrder = orderRepository.save(order);

        // TODO: 주문 생성 시 order_after_create_message 발행

        return CreateOrderResultV1.from(createOrder, FirmInfoV1.from(supplierResult), FirmInfoV1.from(receiverResult));
    }
}
