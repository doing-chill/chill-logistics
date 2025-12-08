package chill_logistics.hub_server.application.dto.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderAfterCommandV1(
    UUID orderId,
    UUID supplierHubId,
    UUID receiverHubId,
    UUID receiverFirmId,
    String receiverFirmFullAddress,
    String receiverFirmOwnerName,
    String requestNote,
    String productName,
    int productQuantity,
    LocalDateTime orderCreatedAt
    ) {}