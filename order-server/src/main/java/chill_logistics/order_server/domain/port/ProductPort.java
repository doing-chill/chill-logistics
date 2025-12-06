package chill_logistics.order_server.domain.port;

import chill_logistics.order_server.application.dto.ProductResultV1;

import java.util.UUID;

public interface ProductPort {
    ProductResultV1 readProductById(UUID productId);
}
