package chill_logistics.hub_server.presentation.dto.request;


import chill_logistics.hub_server.application.dto.command.CreateHubInfoCommandV1;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record CreateHubInfoRequestV1 (

    @NotNull(message = "출발 허브 ID는 필수입니다")
    UUID startHubId,

    @NotNull(message = "도착 허브 ID는 필수입니다")
    UUID endHubId

){
    public CreateHubInfoCommandV1 toCreateHubInfoCommand(){
        return new CreateHubInfoCommandV1(this.endHubId, this.endHubId);

    }


}
