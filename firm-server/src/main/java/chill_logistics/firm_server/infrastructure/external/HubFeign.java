package chill_logistics.firm_server.infrastructure.external;


import chill_logistics.firm_server.infrastructure.external.dto.response.FeignHubResponseV1;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-server")

public interface HubFeign {

    @GetMapping("/v1/hubs/internal/{hubId}")
    boolean readHubInfo(@PathVariable("hubId") UUID hubId);


}
