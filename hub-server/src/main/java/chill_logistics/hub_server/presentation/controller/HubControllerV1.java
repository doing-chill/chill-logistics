package chill_logistics.hub_server.presentation.controller;

import chill_logistics.hub_server.application.service.HubService;
import chill_logistics.hub_server.application.dto.query.HubInfoQueryV1;
import chill_logistics.hub_server.application.dto.query.HubListQueryV1;
import chill_logistics.hub_server.presentation.dto.request.CreateHubRequestV1;
import chill_logistics.hub_server.presentation.dto.request.UpdateHubRequestV1;
import chill_logistics.hub_server.presentation.dto.response.HubInfoResponseV1;
import chill_logistics.hub_server.presentation.dto.response.HubListResponseV1;
import chill_logistics.hub_server.presentation.dto.response.UserHubsResponseV1;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lib.entity.BaseStatus;
import lib.util.SecurityUtils;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PreAuthorize("hasRole('MASTER')")
    public BaseResponse<Void> createHub(@RequestBody @Valid CreateHubRequestV1 createHubRequest) {

        // 본인 userId가 아니라 이미 존재하는 유저인지 확인 후 넣게 작업 필요
        hubService.createHub(SecurityUtils.getCurrentUserId(), createHubRequest.toCreateHubCommand(createHubRequest));

        return BaseResponse.ok(BaseStatus.CREATED);
    }



    // 허브 검색
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    public BaseResponse<List<HubListResponseV1>> readAllHub(

        @RequestParam(required = false) String hubName,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        List<HubListQueryV1> hubListQueries = hubService.readAllHub(hubName, page, size);

        return BaseResponse.ok(HubListResponseV1.fromHubListQuery(hubListQueries), BaseStatus.OK);
    }



    // 단건 조회
    @GetMapping("/{hubId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    public BaseResponse<HubInfoResponseV1> readOneHub(@PathVariable UUID hubId) {

        HubInfoQueryV1 hubInfoQuery = hubService.readOneHub(hubId);

        return BaseResponse.ok(HubInfoResponseV1.from(hubInfoQuery), BaseStatus.OK);
    }



    // 허브 업데이트
    @PatchMapping("/{hubId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('MASTER')")
    public BaseResponse<Void> updateHub(
        @PathVariable UUID hubId, @RequestBody @Valid UpdateHubRequestV1 updateHubRequest) {

        hubService.updateHub(hubId, updateHubRequest.toUpdateHubCommandV1());

        return BaseResponse.ok(BaseStatus.OK);
    }


    @DeleteMapping({"{hubId}"})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('MASTER')")
    public BaseResponse<Void> deleteHub(
        @PathVariable UUID hubId) {

        hubService.deleteHub(SecurityUtils.getCurrentUserId(), hubId);

        return BaseResponse.ok(BaseStatus.OK);
    }


    // 존재하는 허브인지 확인 단순 boolean
    @GetMapping("/internal/{hubId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    public boolean validateHub(@PathVariable UUID hubId) {
        return hubService.validateHub(hubId);
    }


    // 유저 id가 들어오면 해당 유저가 소유한 hubId 반환
    @GetMapping("/userHubs/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    public BaseResponse<List<UserHubsResponseV1>> readUserHubs(@PathVariable UUID userId){
        List<UserHubsResponseV1> userHubsResponse = UserHubsResponseV1.from(hubService.readUserHubs(userId));

        return BaseResponse.ok(userHubsResponse, BaseStatus.OK);

    }


}
