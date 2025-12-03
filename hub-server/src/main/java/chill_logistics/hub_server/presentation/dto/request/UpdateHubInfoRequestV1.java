package chill_logistics.hub_server.presentation.dto.request;

import chill_logistics.hub_server.application.dto.command.UpdateHubInfoCommandV1;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateHubInfoRequestV1(

    @NotNull(message = "출발 허브 ID는 필수입니다")
    UUID startHubId,

    @NotNull(message = "도착 허브 ID는 필수입니다")
    UUID endHubId

) {

    public UpdateHubInfoCommandV1 to(){
        return new UpdateHubInfoCommandV1(this.startHubId, this.endHubId);
    }


}
