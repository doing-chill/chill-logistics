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
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public BaseResponse<Void> createHubInfo(
        //@RequestHeader("User-Id") String userId
        @RequestBody @Valid CreateHubInfoRequestV1 createHubInfoRequest) {

        String userId = String.valueOf(UUID.randomUUID());
        hubInfoService.createHubInfo(UUID.fromString(userId), createHubInfoRequest.toCreateHubInfoCommand());

        return BaseResponse.ok(BaseStatus.CREATED);
    }



    @GetMapping("/{hubInfoId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<HubRoadInfoResponseV1> readHubInfo(
        //@RequestHeader("User-Id") String userId
        @PathVariable UUID hubInfoId) {

        String userId = String.valueOf(UUID.randomUUID());
        HubRoadInfoQueryV1 hubRoadInfoQuery = hubInfoService.readHubInfo(UUID.fromString(userId), hubInfoId);

        return BaseResponse.ok(HubRoadInfoResponseV1.from(hubRoadInfoQuery), BaseStatus.OK);
    }




    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<List<HubRoadInfoListResponseV1>> readAllHubInfo(
        //@RequestHeader("User-Id") String userId
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        String userId = String.valueOf(UUID.randomUUID());
        List<HubRoadInfoListQuery> hubRoadInfoListQueries = hubInfoService.readAllHubInfo(UUID.fromString(userId), page, size);

        return BaseResponse.ok(HubRoadInfoListResponseV1.from(hubRoadInfoListQueries), BaseStatus.OK);
    }



    @PatchMapping("/{hubInfoId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> updateHubInfo(
        //@RequestHeader("User-Id") String userId
        @PathVariable UUID hubInfoId,
        @RequestBody @Valid UpdateHubInfoRequestV1 updateHubInfoRequest){

        String userId = String.valueOf(UUID.randomUUID());
        hubInfoService.updateHubInfo(UUID.fromString(userId), hubInfoId, updateHubInfoRequest.to());

        return BaseResponse.ok(BaseStatus.OK);
    }



    @DeleteMapping("/{hubInfoId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> deleteHubInfo(

        //@RequestHeader("User-Id") String userId

        @PathVariable UUID hubInfoId){

        String userId = String.valueOf(UUID.randomUUID());
        hubInfoService.deleteHubInfo(UUID.fromString(userId), hubInfoId);

        return BaseResponse.ok(BaseStatus.OK);
    }
}
