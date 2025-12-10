package chill_logistics.delivery_server.application;

import chill_logistics.delivery_server.application.dto.command.AssignedDeliveryPersonV1;
import chill_logistics.delivery_server.infrastructure.user.UserClient;
import chill_logistics.delivery_server.infrastructure.user.dto.request.AssignDeliveryAdminRequestDtoV1;
import chill_logistics.delivery_server.infrastructure.user.dto.response.AssignDeliveryAdminResponseDtoV1;
import chill_logistics.delivery_server.presentation.ErrorCode;
import java.util.UUID;
import lib.web.error.BusinessException;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryPersonAssignmentService {

    private final UserClient userClient;

    /* [허브 기반 허브 배송 담당자 배정]
     */
    public AssignedDeliveryPersonV1 assignHubDeliveryPerson(UUID hubId) {

        log.info("[허브배송 담당자 배정 요청] hubId={}", hubId);

        AssignDeliveryAdminRequestDtoV1 request =
            new AssignDeliveryAdminRequestDtoV1(hubId, "HUB");

        BaseResponse<AssignDeliveryAdminResponseDtoV1> response =
            userClient.assignDeliveryAdmin(request);

        if (response == null || response.getData() == null) {
            log.error("[허브배송 담당자 배정 실패] hubId={}", hubId);
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_NOT_FOUND);
        }

        AssignDeliveryAdminResponseDtoV1 data = response.getData();

        log.info("[허브배송 담당자 배정 완료] hubId={}, userId={}, username={}",
            hubId, data.userId(), data.username());

        return new AssignedDeliveryPersonV1(data.userId(), data.username());
    }

    /* [허브 기반 업체 배송 담당자 배정]
     */
    public AssignedDeliveryPersonV1 assignFirmDeliveryPerson(UUID hubId) {

        log.info("[업체배송 담당자 배정 요청] hubId={}", hubId);

        AssignDeliveryAdminRequestDtoV1 request =
            new AssignDeliveryAdminRequestDtoV1(hubId, "FIRM");

        BaseResponse<AssignDeliveryAdminResponseDtoV1> response =
            userClient.assignDeliveryAdmin(request);

        if (response == null || response.getData() == null) {
            log.error("[업체배송 담당자 배정 실패] hubId={}", hubId);
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_NOT_FOUND);
        }

        AssignDeliveryAdminResponseDtoV1 data = response.getData();

        log.info("[업체배송 담당자 배정 완료] hubId={}, userId={}, username={}",
            hubId, data.userId(), data.username());

        return new AssignedDeliveryPersonV1(data.userId(), data.username());
    }
}
