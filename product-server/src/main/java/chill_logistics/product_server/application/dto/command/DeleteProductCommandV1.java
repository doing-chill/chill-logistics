package chill_logistics.product_server.application.dto.command;

import java.util.UUID;

public record DeleteProductCommandV1(
        UUID id
) {}
