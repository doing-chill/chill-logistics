package chill_logistics.user_server.infrastructure.hub;

import chill_logistics.user_server.infrastructure.config.FeignConfig;
import lib.web.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "hub-server",
        configuration = FeignConfig.class
)
public interface HubFeignClientV1 {

    @GetMapping("/v1/hubs/{hubId}")
    BaseResponse<HubInfoResponseV1> getHubInfo(@PathVariable("hubId") UUID hubId);
}
