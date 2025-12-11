package chill_logistics.firm_server.presentation;

import chill_logistics.firm_server.application.service.FirmService;
import chill_logistics.firm_server.domain.entity.FirmType;
import chill_logistics.firm_server.presentation.dto.request.FirmCreateRequestV1;
import chill_logistics.firm_server.presentation.dto.request.FirmUpdateRequestV1;
import chill_logistics.firm_server.presentation.dto.response.FirmInfoListResponseV1;
import chill_logistics.firm_server.presentation.dto.response.FirmInfoResponseV1;
import chill_logistics.firm_server.presentation.dto.response.FirmSearchInfoResponseV1;
import chill_logistics.firm_server.presentation.dto.response.HubSearchIdResponseV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lib.entity.BaseStatus;
import lib.util.SecurityUtils;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/firms")
@Tag(name = "1. 업체 관리", description = "업체 관리용 API")
public class FirmController {

    private final FirmService firmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "업체 추가", description = "업체 추가 API입니다.")
    public BaseResponse<Void> createFirm(@RequestBody @Valid FirmCreateRequestV1 createRequest) {
        firmService.createFirm(createRequest.to());

        return BaseResponse.ok(BaseStatus.CREATED);
    }


    // 업체들 조회
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "업체들 조회", description = "업체들 조회 API입니다.")
    public BaseResponse<List<FirmInfoListResponseV1>> readAllFirm(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

        return BaseResponse.ok(FirmInfoListResponseV1.from(firmService.readAllFirm(page, size)), BaseStatus.OK);
    }

    // 업체 단건 상세 조회
    @GetMapping("/{firmId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "업체 상세 조회", description = "업체 상세 조회 API입니다.")
    public BaseResponse<FirmInfoResponseV1> readFirm(@PathVariable UUID firmId) {

         return BaseResponse.ok(FirmInfoResponseV1.from(firmService.readFirm(firmId)),BaseStatus.OK);
    }

    // 업체 업데이트
    @PatchMapping("/{firmId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "업체 업데이트", description = "업체 업데이트 API입니다.")
    public BaseResponse<Void> updateFirm(@PathVariable UUID firmId, @RequestBody FirmUpdateRequestV1 firmUpdateRequest) {
         firmService.updateFirm(firmId, firmUpdateRequest.to());

         return BaseResponse.ok(BaseStatus.OK);
    }

    // 업체 삭제
    @DeleteMapping("/{firmId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "업체 삭제", description = "업체 삭제 API입니다.")
    public BaseResponse<Void> deleteFirm(@PathVariable UUID firmId) {
        firmService.deleteFirm(SecurityUtils.getCurrentUserId(), firmId);

        return BaseResponse.ok(BaseStatus.OK);
    }



    // 수령 허브 ID 조회
    @GetMapping("/hubIdSearch/{firmId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "업체 Id로 허브 Id 조회", description = "업체 Id로 허브 Id 조회 API입니다.")
    public BaseResponse<HubSearchIdResponseV1> searchFirm(@PathVariable("firmId") UUID firmId) {

        return BaseResponse.ok(HubSearchIdResponseV1.from(firmService.searchFirm(firmId)), BaseStatus.OK);
    }



    // 업체 정보 조회
    @GetMapping("/{firmId}/firmType/{firmType}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    @Operation(summary = "업체 Id로 허브 Id 조회", description = "업체 Id로 허브 Id 조회 API입니다.")
    public BaseResponse<FirmSearchInfoResponseV1> searchFirmInfo(@PathVariable UUID firmId, @PathVariable FirmType firmType) {

        return BaseResponse.ok(FirmSearchInfoResponseV1.from(firmService.searchFirmInfo(firmId, firmType)), BaseStatus.OK);
    }




}
