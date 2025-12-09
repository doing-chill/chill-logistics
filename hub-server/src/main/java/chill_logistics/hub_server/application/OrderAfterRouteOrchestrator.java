package chill_logistics.hub_server.application;


import chill_logistics.hub_server.application.dto.command.OrderAfterCommandV1;
import chill_logistics.hub_server.application.dto.response.HubRouteResultResponse;
import chill_logistics.hub_server.application.vo.HubRouteResult;
import chill_logistics.hub_server.domain.repository.HubRepository;
import chill_logistics.hub_server.infrastructure.external.dto.request.HubRouteAfterCreateV1;
import chill_logistics.hub_server.infrastructure.external.dto.request.HubRouteHubInfoV1;
import chill_logistics.hub_server.infrastructure.service.HubRouteAfterCreateProducer;
import chill_logistics.hub_server.lib.error.ErrorCode;
import chill_logistics.hub_server.presentation.dto.response.HubsRouteInfoResponseV1;
import java.util.List;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderAfterRouteOrchestrator {

    private final HubRouteService hubRouteService;
    private final HubRepository hubRepository;
    private final HubRouteAfterCreateProducer hubRouteAfterCreateProducer;


    @Transactional
    public void findHubRoute(OrderAfterCommandV1 command) {

        hubRepository.findById(command.supplierHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));
        hubRepository.findById(command.receiverHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        HubRouteResult hubRouteResult = hubRouteService.findFastestRouteAndLog(command.supplierHubId(), command.receiverHubId());

        HubRouteResultResponse hubRouteResultResponse = new HubRouteResultResponse(
            command.orderId(), command.receiverFirmId(), command.receiverFirmFullAddress(),
            command.receiverFirmOwnerName(), command.requestNote(), command.productName(),
            command.productQuantity(), command.orderCreatedAt(), hubRouteResult.totalDurationSec(),
            hubRouteResult.totalDistanceKm());

        // vo -> app 계층 dto로
        List<HubsRouteInfoResponseV1> hubsRouteInfoResponse = HubsRouteInfoResponseV1.from(hubRouteResult.steps());

        // 카프카 메시지 전송
        hubRouteAfterCreateProducer.sendHubRouteAfterCreate(HubRouteAfterCreateV1.
            from(hubRouteResultResponse, HubRouteHubInfoV1.from(hubsRouteInfoResponse)));
    }
}
