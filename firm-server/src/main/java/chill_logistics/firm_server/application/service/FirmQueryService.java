package chill_logistics.firm_server.application.service;

import chill_logistics.firm_server.application.dto.query.FirmInfoListQueryV1;
import chill_logistics.firm_server.application.dto.query.FirmInfoQueryV1;
import chill_logistics.firm_server.application.dto.query.FirmSearchInfoQueryV1;
import chill_logistics.firm_server.application.dto.query.HubSearchQueryV1;
import chill_logistics.firm_server.domain.entity.Firm;
import chill_logistics.firm_server.domain.entity.FirmType;
import chill_logistics.firm_server.domain.repository.FirmRepository;
import chill_logistics.firm_server.lib.error.ErrorCode;
import java.util.List;
import java.util.UUID;
import lib.pagination.CustomPageRequest;
import lib.pagination.CustomPageResult;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FirmQueryService {

    private final FirmRepository firmRepository;

    @Transactional(readOnly = true)
    public HubSearchQueryV1 searchFirm(UUID firmId) {

        Firm firm = firmRepository.findById(firmId)
            .orElseThrow(() -> new BusinessException(ErrorCode.FIRM_NOT_FOUND));

        return new HubSearchQueryV1(firm.getHubId());
    }


    @Transactional(readOnly = true)
    public FirmSearchInfoQueryV1 searchFirmHubInfo(UUID firmId, FirmType firmType) {

        Firm firm = firmRepository.findByIdAndFirmTypeAndDeletedAtIsNull(firmId, firmType)
            .orElseThrow(() -> new BusinessException(ErrorCode.FIRM_NOT_FOUND));

        return FirmSearchInfoQueryV1.from(firm.getId(), firm.getName(), firm.getHubId(), firm.getFullAddress(), firm.getOwnerName());
    }

    // 업체들 조회
    @Transactional(readOnly = true)
    public FirmInfoListQueryV1 readAllFirm(int page, int size) {

        CustomPageRequest customPageRequest = CustomPageRequest.of(page, size, 0, 10);

        CustomPageResult<Firm> firmList = firmRepository.findAll(customPageRequest);

        return FirmInfoListQueryV1.from(firmList);
    }

    // 업체 단건 상세 조회
    @Transactional(readOnly = true)
    public FirmInfoQueryV1 readFirm(UUID firmId) {

        Firm firm = firmRepository.findById(firmId)
            .orElseThrow(() -> new BusinessException(ErrorCode.FIRM_NOT_FOUND));

        return FirmInfoQueryV1.from(firm);
    }

}
