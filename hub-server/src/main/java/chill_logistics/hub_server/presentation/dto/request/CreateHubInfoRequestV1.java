package chill_logistics.hub_server.presentation.dto.request;


import chill_logistics.hub_server.application.dto.command.CreateHubInfoCommandV1;
import chill_logistics.hub_server.lib.error.ErrorCode;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lib.web.error.BusinessException;

public record CreateHubInfoRequestV1 (

    @NotNull(message = "출발 허브 ID는 필수입니다")
    UUID startHubId,

    @NotNull(message = "도착 허브 ID는 필수입니다")
    UUID endHubId

){

    public CreateHubInfoRequestV1 {
        // null일 수 있으니 방어 코드
        if (startHubId.equals(endHubId)) {
            throw new BusinessException(ErrorCode.HUB_INFO_START_EQUALS_END);
        }
    }

    public CreateHubInfoCommandV1 toCreateHubInfoCommand(){
        return new CreateHubInfoCommandV1(this.startHubId, this.endHubId);
    }


}
