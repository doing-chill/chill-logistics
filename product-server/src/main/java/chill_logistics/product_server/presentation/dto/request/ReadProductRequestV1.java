package chill_logistics.product_server.presentation.dto.request;

import chill_logistics.product_server.application.dto.query.ReadProductCommandV1;

import java.util.UUID;

public record ReadProductRequestV1(
        String name,
        UUID firmId,
        UUID hubId,
        Boolean sellable
) {
    public ReadProductCommandV1 toCommand() {
        return ReadProductCommandV1.builder()
                .name(this.name)
                .firmId(this.firmId)
                .hubId(this.hubId)
                .sellable(this.sellable)
                .build();
    }
}
