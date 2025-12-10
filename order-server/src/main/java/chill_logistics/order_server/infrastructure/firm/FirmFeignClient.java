package chill_logistics.order_server.infrastructure.firm;

import chill_logistics.order_server.infrastructure.config.FeignConfig;
import chill_logistics.order_server.infrastructure.firm.dto.FirmResponseV1;
import lib.web.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "firm-server", configuration = FeignConfig.class)
public interface FirmFeignClient {

    @GetMapping("/v1/firms/{firmId}/firmType/{firmType}")
    BaseResponse<FirmResponseV1> searchFirmInfo(
            @PathVariable UUID firmId,
            @PathVariable String firmType);
}
