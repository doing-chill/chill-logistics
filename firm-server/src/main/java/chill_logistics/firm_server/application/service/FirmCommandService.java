package chill_logistics.firm_server.application.service;

import chill_logistics.firm_server.application.dto.command.FirmCreateCommandV1;
import chill_logistics.firm_server.application.dto.command.FirmUpdateCommandV1;
import chill_logistics.firm_server.application.dto.response.UserResponseV1;
import chill_logistics.firm_server.application.port.HubClient;
import chill_logistics.firm_server.application.port.UserClient;
import chill_logistics.firm_server.domain.entity.Firm;
import chill_logistics.firm_server.domain.repository.FirmRepository;
import chill_logistics.firm_server.lib.error.ErrorCode;
import feign.FeignException.FeignClientException;
import java.math.BigDecimal;
import java.util.UUID;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FirmCommandService {

    private final FirmRepository firmRepository;
    private final UserClient userClient;
    private final HubClient hubClient;

    @Transactional
    public void createFirm(FirmCreateCommandV1 command) {

        // 기존에 있는 유저인지 검증 + userName 가져오기
        UserResponseV1 userResponse;
        try {
            userResponse = userClient.readUserInfo(command.ownerId()).getData().to();
        }catch (FeignClientException e){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        duplicateFirm(command.hubId(), command.name(), command.fullAddress(), command.latitude(), command.longitude());

        Firm firm = Firm.create(
            command.name(),
            command.hubId(),
            command.ownerId(),
            userResponse.username(),
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

    @Transactional
    public void updateFirm(UUID firmId, FirmUpdateCommandV1 command) {
        Firm firm = firmRepository.findById(firmId)
            .orElseThrow(() -> new BusinessException(ErrorCode.FIRM_NOT_FOUND));

        duplicateFirm(command.hubId(), command.name(), command.fullAddress(), command.latitude(), command.longitude());

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

    @Transactional
    public void deleteFirm(UUID userId, UUID firmId) {
        Firm firm = firmRepository.findById(firmId)
            .orElseThrow(() -> new BusinessException(ErrorCode.FIRM_NOT_FOUND));

        firm.delete(userId);
    }

    private void duplicateFirm(UUID hubId, String firmName, String fullAddress, BigDecimal latitude, BigDecimal longitude) {
        // 기존에 존재하는 허브인지 검증
        if (! hubClient.readHubInfo(hubId)){
            throw new BusinessException(ErrorCode.HUB_NOT_FOUND);
        }

        // 기존에 이름이 이미 있는 경우
        if (firmRepository.existsByNameAndDeletedAtIsNull(firmName)){
            throw new BusinessException(ErrorCode.HUB_ALREADY_EXISTS);
        }

        // fullAddress가 중복인지
        if (firmRepository.existsByFullAddressAndDeletedAtIsNull(fullAddress)){
            throw new BusinessException(ErrorCode.HUB_ALREADY_EXISTS);
        }

        // 위도 and 경도가 겹치는 업체가 있는지
        if (firmRepository.existsByLatitudeAndLongitudeAndDeletedAtIsNull(latitude, longitude)){
            throw new BusinessException(ErrorCode.HUB_ALREADY_EXISTS);
        }
    }

}
