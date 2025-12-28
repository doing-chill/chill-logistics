package chill_logistics.hub_server.presentation.controller;

import chill_logistics.hub_server.application.service.HubInfoFacade;
import chill_logistics.hub_server.application.dto.query.HubRoadInfoListQuery;
import chill_logistics.hub_server.application.dto.query.HubRoadInfoQueryV1;
import chill_logistics.hub_server.presentation.dto.request.CreateHubInfoRequestV1;
import chill_logistics.hub_server.presentation.dto.request.UpdateHubInfoRequestV1;
import chill_logistics.hub_server.presentation.dto.response.HubRoadInfoListResponseV1;
import chill_logistics.hub_server.presentation.dto.response.HubRoadInfoResponseV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lib.entity.BaseStatus;
import lib.util.SecurityUtils;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/hub-infos")
@RequiredArgsConstructor
@Tag(name = "2. 허브 경로 관리", description = "허브 경로 관리용 API")
// 허브 경로
public class HubInfoControllerV1 {

    private final HubInfoFacade hubInfoFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "허브 경로 추가", description = "허브 경로 추가 API입니다.")
    public BaseResponse<Void> createHubInfo(

        @RequestBody @Valid CreateHubInfoRequestV1 createHubInfoRequest) {

        hubInfoFacade.createHubInfo(createHubInfoRequest.toCreateHubInfoCommand());

        return BaseResponse.ok(BaseStatus.CREATED);
    }

    @GetMapping("/{hubInfoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'HUB_DELIVERY_MANAGER', 'FIRM_DELIVERY_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "허브 경로 상세 조회", description = "허브 경로 상세 조회 API입니다.")
    public BaseResponse<HubRoadInfoResponseV1> readHubInfo(
        @PathVariable UUID hubInfoId) {

        HubRoadInfoQueryV1 hubRoadInfoQuery = hubInfoFacade.readHubInfo(hubInfoId);

        return BaseResponse.ok(HubRoadInfoResponseV1.from(hubRoadInfoQuery), BaseStatus.OK);
    }



    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'HUB_DELIVERY_MANAGER', 'FIRM_DELIVERY_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "허브 경로들 조회", description = "허브 경로들 조회 API입니다.")
    public BaseResponse<List<HubRoadInfoListResponseV1>> readAllHubInfo(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        List<HubRoadInfoListQuery> hubRoadInfoListQueries = hubInfoFacade.readAllHubInfo(page, size);

        return BaseResponse.ok(HubRoadInfoListResponseV1.from(hubRoadInfoListQueries), BaseStatus.OK);
    }

    @PatchMapping("/{hubInfoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "허브 경로 수정", description = "허브 경로 수정 API입니다.")
    public BaseResponse<Void> updateHubInfo(
        @PathVariable UUID hubInfoId,
        @RequestBody @Valid UpdateHubInfoRequestV1 updateHubInfoRequest){

        hubInfoFacade.updateHubInfo(hubInfoId, updateHubInfoRequest.to());

        return BaseResponse.ok(BaseStatus.OK);
    }

    @DeleteMapping("/{hubInfoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "허브 경로 삭제", description = "허브 경로 삭제 API입니다.")
    public BaseResponse<Void> deleteHubInfo(

        @PathVariable UUID hubInfoId){

        hubInfoFacade.deleteHubInfo(SecurityUtils.getCurrentUserId(), hubInfoId);

        return BaseResponse.ok(BaseStatus.OK);
    }
}
