package chill_logistics.firm_server.application.service;

import chill_logistics.firm_server.application.dto.command.FirmCreateCommandV1;
import chill_logistics.firm_server.application.dto.command.FirmUpdateCommandV1;
import chill_logistics.firm_server.application.dto.query.FirmInfoListQueryV1;
import chill_logistics.firm_server.application.dto.query.FirmInfoQueryV1;
import chill_logistics.firm_server.application.dto.query.FirmSearchInfoQueryV1;
import chill_logistics.firm_server.application.dto.query.HubSearchQueryV1;
import chill_logistics.firm_server.application.port.HubClient;
import chill_logistics.firm_server.application.port.UserClient;
import chill_logistics.firm_server.domain.entity.Firm;
import chill_logistics.firm_server.domain.entity.FirmType;
import chill_logistics.firm_server.domain.repository.FirmRepository;
import chill_logistics.firm_server.lib.error.ErrorCode;
import chill_logistics.firm_server.presentation.dto.request.FirmUpdateRequestV1;
import java.util.List;
import java.util.UUID;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FirmService {

    private final FirmRepository firmRepository;
    private final UserClient userClient;
    private final HubClient hubClient;

    @Transactional
    public void createFirm(FirmCreateCommandV1 command) {

//        // 기존에 있는 유저인지 검증
//        try {
//            UserResponseV1 userResponseV1 = userClient.readUserInfo(command.ownerId()).to();
//        }catch (FeignClientException e){
//            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
//        }
        // TODO 지워야 함
        String userName = "gd";

        // 기존에 존재하는 허브인지 검증
        if (! hubClient.readHubInfo(command.hubId())){
            throw new BusinessException(ErrorCode.HUB_NOT_FOUND);
        }

        // 기존에 이름이 이미 있는 경우
        if (firmRepository.existsByNameAndDeletedAtIsNull(command.name())){
            throw new BusinessException(ErrorCode.HUB_ALREADY_EXISTS);
        }

        // fulladdress가 중복인지
        if (firmRepository.existsByFullAddressAndDeletedAtIsNull(command.fullAddress())){
            throw new BusinessException(ErrorCode.HUB_ALREADY_EXISTS);
        }

        // 위도 and 경도가 겹치는 업체가 있는지
        if (firmRepository.existsByLatitudeAndLongitudeAndDeletedAtIsNull(command.latitude(), command.longitude())){
            throw new BusinessException(ErrorCode.HUB_ALREADY_EXISTS);
        }

        Firm firm = Firm.create(
            command.name(),
            command.hubId(),
            command.ownerId(),
            //userResponseV1.username(),
            userName,
            command.firmType(),
            command.postalCode(),
            command.country(),
            command.region(),
            command.city(),
            command.district(),
            command.roadName(),
            command.buildingName(),
            command.detailAddress(),
            command.fullAddress(),
            command.latitude(),
            command.longitude()
        );

        firmRepository.save(firm);
    }


    @Transactional(readOnly = true)
    public HubSearchQueryV1 searchFirm(UUID firmId) {

        Firm firm = firmRepository.findById(firmId)
            .orElseThrow(() -> new BusinessException(ErrorCode.FIRM_NOT_FOUND));

        return new HubSearchQueryV1(firm.getHubId());
    }


    @Transactional(readOnly = true)
    public FirmSearchInfoQueryV1 searchFirmInfo(UUID firmId, FirmType firmType) {

        Firm firm = firmRepository.findByIdAndFirmTypeAndDeletedAtIsNull(firmId, firmType)
            .orElseThrow(() -> new BusinessException(ErrorCode.FIRM_NOT_FOUND));

        return FirmSearchInfoQueryV1.from(firm.getId(), firm.getName(), firm.getHubId(), firm.getFullAddress(), firm.getOwnerName());
    }

    @Transactional(readOnly = true)
    public List<FirmInfoListQueryV1> readAllFirm(int page, int size) {

        List<Firm> firmList = firmRepository.findAll(page, size);

        return FirmInfoListQueryV1.from(firmList);
    }

    @Transactional(readOnly = true)
    public FirmInfoQueryV1 readFirm(UUID firmId) {

        Firm firm = firmRepository.findById(firmId)
            .orElseThrow(() -> new BusinessException(ErrorCode.FIRM_NOT_FOUND));

        return FirmInfoQueryV1.from(firm);
    }

    @Transactional
    public void updateFirm(UUID firmId, FirmUpdateCommandV1 command) {
        Firm firm = firmRepository.findById(firmId)
            .orElseThrow(() -> new BusinessException(ErrorCode.FIRM_NOT_FOUND));

        if (firm.getHubId() != command.hubId()){
            // 기존에 존재하는 허브인지 검증
            if (! hubClient.readHubInfo(command.hubId())){
                throw new BusinessException(ErrorCode.HUB_NOT_FOUND);
            }
        }

        firm.update(
            command.name(),
            command.hubId(),
            command.ownerId(),
            command.firmType(),
            command.postalCode(),
            command.country(),
            command.region(),
            command.city(),
            command.district(),
            command.roadName(),
            command.buildingName(),
            command.detailAddress(),
            command.fullAddress(),
            command.latitude(),
            command.longitude()
        );
    }


}
