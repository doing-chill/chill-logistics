package chill_logistics.firm_server.presentation;

import chill_logistics.firm_server.application.service.FirmService;
import chill_logistics.firm_server.domain.entity.FirmType;
import chill_logistics.firm_server.presentation.dto.request.FirmCreateRequestV1;
import chill_logistics.firm_server.presentation.dto.response.FirmInfoListResponseV1;
import chill_logistics.firm_server.presentation.dto.response.FirmInfoResponseV1;
import chill_logistics.firm_server.presentation.dto.response.FirmSearchInfoResponseV1;
import chill_logistics.firm_server.presentation.dto.response.HubSearchIdResponseV1;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
public class FirmController {

    private final FirmService firmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MASTER')")
    public BaseResponse<Void> createFirm(@RequestBody @Valid FirmCreateRequestV1 createRequest) {
        firmService.createFirm(createRequest.to());

        return BaseResponse.ok(BaseStatus.CREATED);
    }


    // 업체들 조회
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    public BaseResponse<List<FirmInfoListResponseV1>> readAllFirm(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

        return BaseResponse.ok(FirmInfoListResponseV1.from(firmService.readAllFirm(page, size)), BaseStatus.OK);
    }

    // 업체 단건 상세 조회
    @GetMapping("/firmId")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    public BaseResponse<FirmInfoResponseV1> readFirm(@RequestParam UUID firmId) {

         return BaseResponse.ok(FirmInfoResponseV1.from(firmService.readFirm(firmId)),BaseStatus.OK);
    }

    // 업체 업데이트


    //업체 삭제



    // 수령 허브 ID 조회
    @GetMapping("/hubIdSearch/{firmId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    public BaseResponse<HubSearchIdResponseV1> searchFirm(@PathVariable("firmId") UUID firmId) {

        return BaseResponse.ok(HubSearchIdResponseV1.from(firmService.searchFirm(firmId)), BaseStatus.OK);
    }



    // 업체 정보 조회
    @GetMapping("/{firmId}/firmType/{firmType}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'FIRM_MANAGER')")
    public BaseResponse<FirmSearchInfoResponseV1> searchFirmInfo(@PathVariable UUID firmId, @PathVariable FirmType firmType) {

        return BaseResponse.ok(FirmSearchInfoResponseV1.from(firmService.searchFirmInfo(firmId, firmType)), BaseStatus.OK);
    }




}
