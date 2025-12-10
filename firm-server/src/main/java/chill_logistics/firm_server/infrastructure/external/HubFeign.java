package chill_logistics.firm_server.infrastructure.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-server")

public interface HubFeign {

    @GetMapping("/v1/hubs/internal/{hubId}")
    boolean readHubInfo(@PathVariable("hubId") UUID hubId);


}
