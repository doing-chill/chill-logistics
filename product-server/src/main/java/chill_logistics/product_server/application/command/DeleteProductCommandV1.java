package chill_logistics.product_server.application.command;

import java.util.UUID;

public record DeleteProductCommandV1(
        UUID id
) { }
