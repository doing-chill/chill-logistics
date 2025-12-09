package chill_logistics.order_server.infrastructure.firm;

import chill_logistics.order_server.application.dto.command.FirmResultV1;
import chill_logistics.order_server.domain.port.FirmPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FirmApiClient implements FirmPort {

    private final FirmFeignClient firmFeignClient;

    @Override
    public FirmResultV1 readFirmById(UUID firmId, String firmType) {
        return null;
    }

    @Override
    public UUID readHubId(UUID receiverFirmId) {
        return null;
    }
}
