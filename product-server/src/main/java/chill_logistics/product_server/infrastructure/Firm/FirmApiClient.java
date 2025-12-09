package chill_logistics.product_server.infrastructure.Firm;

import chill_logistics.product_server.domain.port.FirmPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FirmApiClient implements FirmPort {

    private final FirmFeignClient firmFeignClient;

    @Override
    public void validateExists(UUID firmId) {

    }
}
