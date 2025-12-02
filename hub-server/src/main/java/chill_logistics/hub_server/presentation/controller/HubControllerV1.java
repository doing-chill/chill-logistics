package chill_logistics.hub_server.presentation.controller;

import chill_logistics.hub_server.application.HubService;
import chill_logistics.hub_server.application.dto.query.HubInfoQueryV1;
import chill_logistics.hub_server.application.dto.query.HubListQueryV1;
import chill_logistics.hub_server.presentation.dto.request.CreateHubRequestV1;
import chill_logistics.hub_server.presentation.dto.request.UpdateHubRequestV1;
import chill_logistics.hub_server.presentation.dto.response.HubInfoResponseV1;
import chill_logistics.hub_server.presentation.dto.response.HubListResponseV1;
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
import org.springframework.web.bind.annotation.PutMapping;
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
        //@RequestHeader("User-Id") String userId,
        @RequestBody @Valid CreateHubRequestV1 createHubRequest) {

        String userId = String.valueOf(UUID.randomUUID());

        hubService.createHub(UUID.fromString(userId), CreateHubRequestV1.toCreateHubCommand(createHubRequest));
        return BaseResponse.ok(BaseStatus.CREATED);
    }

    // 허브 검색
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<List<HubListResponseV1>> readAllHub(
        //@RequestHeader("User-Id") String userId,
        @RequestParam(required = false) String hubName,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        String userId = String.valueOf(UUID.randomUUID());

        List<HubListQueryV1> hubListQueries = hubService.readAllHub(UUID.fromString(userId), hubName, page, size);
        return BaseResponse.ok(HubListResponseV1.fromHubListQuery(hubListQueries), BaseStatus.OK);
    }



    // 단건 조회
    @GetMapping("/{hubId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<HubInfoResponseV1> readOneHub(
        //@RequestHeader("User-Id") String userId,
        @PathVariable UUID hubId) {

        // TODO 지워야 함
        String userId = String.valueOf(UUID.randomUUID());

        HubInfoQueryV1 hubInfoQuery = hubService.readOneHub(UUID.fromString(userId), hubId);
        return BaseResponse.ok(HubInfoResponseV1.from(hubInfoQuery), BaseStatus.OK);
    }

    // 허브 업데이트
    @PatchMapping("/{hubId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> updateHub(
        //@RequestHeader("User-Id") String userId,
        @PathVariable UUID hubId,
    @RequestBody @Valid UpdateHubRequestV1 updateHubRequest){

        String userId = String.valueOf(UUID.randomUUID());

        hubService.updateHub(UUID.fromString(userId), hubId, updateHubRequest.toUpdateHubCommandV1());
        return BaseResponse.ok(BaseStatus.OK);
    }


    @DeleteMapping({"{hubId}"})
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> deleteHub(
        //@RequestHeader("User-Id") String userId,
        @PathVariable UUID hubId) {

        String userId = String.valueOf(UUID.randomUUID());

        hubService.deleteHub(UUID.fromString(userId), hubId);
        return BaseResponse.ok(BaseStatus.OK);

    }




    // 존재하는 허브인지 확인
    @GetMapping("/check/{hubId}")
    public boolean validateHub(@PathVariable UUID hubId) {
        return hubService.validateHub(hubId);
    }


}
