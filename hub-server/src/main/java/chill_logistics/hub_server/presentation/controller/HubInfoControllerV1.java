package chill_logistics.hub_server.presentation.controller;

import chill_logistics.hub_server.application.HubInfoService;
import chill_logistics.hub_server.application.dto.query.HubRoadInfoListQuery;
import chill_logistics.hub_server.application.dto.query.HubRoadInfoQueryV1;
import chill_logistics.hub_server.presentation.dto.request.CreateHubInfoRequestV1;
import chill_logistics.hub_server.presentation.dto.request.UpdateHubInfoRequestV1;
import chill_logistics.hub_server.presentation.dto.response.HubRoadInfoListResponseV1;
import chill_logistics.hub_server.presentation.dto.response.HubRoadInfoResponseV1;
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
@RestController
@RequestMapping("/v1/hub-infos")
@RequiredArgsConstructor
// 허브 경로
public class HubInfoControllerV1 {

    private final HubInfoService hubInfoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MASTER')")
    public BaseResponse<Void> createHubInfo(

        @RequestBody @Valid CreateHubInfoRequestV1 createHubInfoRequest) {

        hubInfoService.createHubInfo(createHubInfoRequest.toCreateHubInfoCommand());

        return BaseResponse.ok(BaseStatus.CREATED);
    }


    @GetMapping("/{hubInfoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    public BaseResponse<HubRoadInfoResponseV1> readHubInfo(
        @PathVariable UUID hubInfoId) {

        HubRoadInfoQueryV1 hubRoadInfoQuery = hubInfoService.readHubInfo(hubInfoId);

        return BaseResponse.ok(HubRoadInfoResponseV1.from(hubRoadInfoQuery), BaseStatus.OK);
    }



    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    public BaseResponse<List<HubRoadInfoListResponseV1>> readAllHubInfo(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        List<HubRoadInfoListQuery> hubRoadInfoListQueries = hubInfoService.readAllHubInfo(page, size);

        return BaseResponse.ok(HubRoadInfoListResponseV1.from(hubRoadInfoListQueries), BaseStatus.OK);
    }



    @PatchMapping("/{hubInfoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('MASTER')")
    public BaseResponse<Void> updateHubInfo(
        @PathVariable UUID hubInfoId,
        @RequestBody @Valid UpdateHubInfoRequestV1 updateHubInfoRequest){

        hubInfoService.updateHubInfo(hubInfoId, updateHubInfoRequest.to());

        return BaseResponse.ok(BaseStatus.OK);
    }



    @DeleteMapping("/{hubInfoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('MASTER')")
    public BaseResponse<Void> deleteHubInfo(

        @PathVariable UUID hubInfoId){

        hubInfoService.deleteHubInfo(SecurityUtils.getCurrentUserId(), hubInfoId);

        return BaseResponse.ok(BaseStatus.OK);
    }
}
