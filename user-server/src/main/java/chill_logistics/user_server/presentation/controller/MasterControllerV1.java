package chill_logistics.user_server.presentation.controller;

import chill_logistics.user_server.application.dto.command.MasterUpdateSignupStatusCommandV1;
import chill_logistics.user_server.application.dto.command.MasterUpdateUserInfoCommandV1;
import chill_logistics.user_server.application.dto.query.MasterSignupUserQueryV1;
import chill_logistics.user_server.application.dto.query.MasterUserInfoListQueryV1;
import chill_logistics.user_server.application.dto.query.MasterUserInfoQueryV1;
import chill_logistics.user_server.application.service.MasterQueryServiceV1;
import chill_logistics.user_server.application.service.MasterCommandServiceV1;
import chill_logistics.user_server.presentation.dto.request.MasterUpdateSignupStatusRequestDtoV1;
import chill_logistics.user_server.presentation.dto.request.MasterUpdateUserInfoRequestDtoV1;
import chill_logistics.user_server.presentation.dto.response.MasterSignupUserResponseDtoV1;
import chill_logistics.user_server.presentation.dto.response.MasterUserInfoListResponseDtoV1;
import chill_logistics.user_server.presentation.dto.response.MasterUserInfoResponseDtoV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/master/users")
@SecurityRequirement(name = "bearerAuth")   // 컨트롤러단에서 JWT 인증을 적용하고 싶으면 이렇게 (메소드 단으로도 설정 가능)
@Tag(name = "2.MASTER - 유저 관리", description = "마스터 계정이 사용하는 유저 관리용 API")             // (필수)
@Slf4j
public class MasterControllerV1 {

    private final MasterCommandServiceV1 masterService;
    private final MasterQueryServiceV1 masterQueryService;

    @PutMapping("/{userId}/info")
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "유저 정보 수정", description = "MASTER 계정이 특정 유저의 정보를 수정할 때 사용하는 API입니다.")     // 메서드에 대한 부분 설명 가능 (필수)
    public BaseResponse<Void> updateUserInfo(
            @Parameter(description = "수정 대상 유저 ID (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")   // 파라미터 부분도 설명 가능 (선택)
            @PathVariable UUID userId,
            @RequestBody MasterUpdateUserInfoRequestDtoV1 request) {

        MasterUpdateUserInfoCommandV1 command = request.toCommand(userId);
        masterService.updateUserInfo(command);

        return BaseResponse.ok(BaseStatus.OK);
    }

    @GetMapping("/signup")
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "회원가입 유저 목록 조회", description = "MASTER 계정이 회원가입한 유저들의 목록을 조회할 때 사용하는 API입니다.")
    public BaseResponse<List<MasterSignupUserResponseDtoV1>> getSignupUserList() {

        List<MasterSignupUserQueryV1> resultList = masterQueryService.readSignupUserList();

        List<MasterSignupUserResponseDtoV1> responseList = MasterSignupUserResponseDtoV1.from(resultList);

        return BaseResponse.ok(responseList, BaseStatus.OK);
    }

    @PutMapping("/{userId}/signup")
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "회원가입 승인/거절 처리", description = "MASTER 계정이 특정 유저의 회원가입 상태를 승인 또는 거절 처리하는 API입니다.")
    public BaseResponse<Void> updateSignupStatus(
            @PathVariable UUID userId,
            @RequestBody MasterUpdateSignupStatusRequestDtoV1 request) {

        MasterUpdateSignupStatusCommandV1 command = request.toCommand(userId);
        masterService.updateSignupStatus(command);

        return BaseResponse.ok(BaseStatus.OK);
    }

    @GetMapping("/info")
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "유저 정보 전체 조회", description = "MASTER 계정이 유저 전체 정보를 조회하는 API")
    public BaseResponse<List<MasterUserInfoListResponseDtoV1>> readUserInfoList() {

        List<MasterUserInfoListQueryV1> resultList = masterQueryService.readUserInfoList();
        List<MasterUserInfoListResponseDtoV1> responseList = MasterUserInfoListResponseDtoV1.from(resultList);

        return BaseResponse.ok(responseList, BaseStatus.OK);
    }

    @GetMapping("/{userId}/info")
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "유저 정보 단건 조회", description = "MASTER 계정이 특정 유저의 상세 정보를 조회하는 API입니다.")
    public BaseResponse<MasterUserInfoResponseDtoV1> readUserInfo(@PathVariable UUID userId) {

        MasterUserInfoQueryV1 result = masterQueryService.readUserInfo(userId);
        MasterUserInfoResponseDtoV1 response = MasterUserInfoResponseDtoV1.from(result);

        return BaseResponse.ok(response, BaseStatus.OK);
    }
}
