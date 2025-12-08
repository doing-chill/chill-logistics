package chill_logistics.hub_server.application;


import chill_logistics.hub_server.application.dto.command.OrderAfterCommandV1;
import chill_logistics.hub_server.application.dto.response.HubRouteResultResponse;
import chill_logistics.hub_server.application.vo.HubRouteResult;
import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.repository.HubRepository;
import chill_logistics.hub_server.infrastructure.config.kafka.KafkaProducerConfig;
import chill_logistics.hub_server.infrastructure.external.dto.response.HubRouteAfterCreateV1;
import chill_logistics.hub_server.infrastructure.service.HubRouteAfterCreateProducer;
import chill_logistics.hub_server.lib.error.ErrorCode;
import java.math.BigDecimal;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderAfterRouteOrchestrator {

    private final HubRouteService hubRouteService;
    private final HubRepository hubRepository;
    private final HubRouteAfterCreateProducer hubRouteAfterCreateProducer;


    public void findHubRoute(OrderAfterCommandV1 command) {

        Hub startHub = hubRepository.findById(command.supplierHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));
        Hub endHub = hubRepository.findById(command.receiverHubId())
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        HubRouteResult hubRouteResult = hubRouteService.findFastestRouteAndLog(command.supplierHubId(), command.receiverHubId());

        HubRouteResultResponse hubRouteResultResponse = new HubRouteResultResponse(
            command.orderId(), hubRouteResult.startHubId(), startHub.getName(),
            startHub.getFullAddress(), hubRouteResult.endHubId(), endHub.getName(),
            endHub.getFullAddress(), command.receiverFirmId(), command.receiverFirmFullAddress(),
            command.receiverFirmOwnerName(), command.requestNote(), command.productName(),
            command.productQuantity(), command.orderCreatedAt(), hubRouteResult.totalDurationSec(), hubRouteResult.pathHubIds(),
            hubRouteResult.totalDistanceKm());

        // 카프카 메시지 전송
        hubRouteAfterCreateProducer.sendHubRouteAfterCreate(HubRouteAfterCreateV1.from(hubRouteResultResponse));
    }




}
