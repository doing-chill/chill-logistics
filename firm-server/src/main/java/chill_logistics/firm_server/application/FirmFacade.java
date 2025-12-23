package chill_logistics.firm_server.application;


import chill_logistics.firm_server.application.dto.command.FirmCreateCommandV1;
import chill_logistics.firm_server.application.dto.command.FirmUpdateCommandV1;
import chill_logistics.firm_server.application.dto.query.FirmInfoListQueryV1;
import chill_logistics.firm_server.application.dto.query.FirmInfoQueryV1;
import chill_logistics.firm_server.application.dto.query.FirmSearchInfoQueryV1;
import chill_logistics.firm_server.application.dto.query.HubSearchQueryV1;
import chill_logistics.firm_server.application.service.FirmCommandService;
import chill_logistics.firm_server.application.service.FirmQueryService;
import chill_logistics.firm_server.domain.entity.FirmType;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FirmFacade {

    public final FirmCommandService firmCommandService;
    public final FirmQueryService firmQueryService;


    public void createFirm(FirmCreateCommandV1 command){
        firmCommandService.createFirm(command);
    }


    public HubSearchQueryV1 searchFirm(UUID firmId) {
        return firmQueryService.searchFirm(firmId);
    }

    public void updateFirm(UUID firmId, FirmUpdateCommandV1 command){
        firmCommandService.updateFirm(firmId, command);
    }

    public void deleteFirm(UUID userId, UUID firmId){
        firmCommandService.deleteFirm(userId, firmId );
    }

    public FirmSearchInfoQueryV1 searchFirmHubInfo(UUID firmId, FirmType firmType) {
        return firmQueryService.searchFirmHubInfo(firmId, firmType);
    }

    public List<FirmInfoListQueryV1> readAllFirm(int page, int size) {
        return firmQueryService.readAllFirm(page, size);
    }

    public FirmInfoQueryV1 readFirm(UUID firmId) {
        return firmQueryService.readFirm(firmId);
    }

}
