package chill_logistics.hub_server.presentation.controller;

import chill_logistics.hub_server.application.HubService;
import chill_logistics.hub_server.presentation.dto.request.CreateHubRequestV1;
import chill_logistics.hub_server.presentation.dto.response.HubInfoResponseV1;
import chill_logistics.hub_server.presentation.dto.response.HubListResponseV1;
import java.util.List;
import java.util.UUID;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/hubs")
public class HubControllerV1 {

    private final HubService hubService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Void> createHub(
        @RequestHeader("User-Id") String userId, @RequestBody CreateHubRequestV1 createHubRequest) {
        hubService.createHub(UUID.fromString(userId), CreateHubRequestV1.toCreateHubCommand(createHubRequest));
        return BaseResponse.ok(BaseStatus.CREATED);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<List<HubListResponseV1>> readAllHub(
        @RequestHeader("User-Id") String userId,
        @RequestParam(required = false) String hubName,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        List<HubListResponseV1> hubListResponse = hubService.readAllHub(UUID.fromString(userId), hubName, page, size);
        return BaseResponse.ok(hubListResponse, BaseStatus.OK);
    }




    @GetMapping("/{hubId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<HubInfoResponseV1> readOneHub(
        @RequestHeader("User-Id") String userId, @PathVariable UUID hubId) {
        HubInfoResponseV1 hubInfoResponse = hubService.readOneHub(UUID.fromString(userId), hubId);
        return BaseResponse.ok(hubInfoResponse, BaseStatus.OK );
    }



}
