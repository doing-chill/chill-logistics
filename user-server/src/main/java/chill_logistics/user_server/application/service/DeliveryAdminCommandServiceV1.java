package chill_logistics.user_server.application.service;

import chill_logistics.user_server.application.dto.command.AssignDeliveryAdminCommandV1;
import chill_logistics.user_server.domain.entity.DeliveryAdmin;
import chill_logistics.user_server.domain.entity.DeliveryPossibility;
import chill_logistics.user_server.domain.entity.User;
import chill_logistics.user_server.domain.repository.DeliveryAdminRepository;
import chill_logistics.user_server.domain.repository.UserRepository;
import chill_logistics.user_server.presentation.dto.response.AssignDeliveryAdminResponseDtoV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryAdminCommandServiceV1 {

    private final DeliveryAdminRepository deliveryAdminRepository;
    private final UserRepository userRepository;

    @Transactional
    public AssignDeliveryAdminResponseDtoV1 assignDeliveryAdmin(AssignDeliveryAdminCommandV1 command) {

        DeliveryAdmin deliveryAdmin = deliveryAdminRepository.deliveryManagerAssign(
                command.hubId(),
                command.deliveryAdminType(),
                DeliveryPossibility.POSSIBLE
        ).orElseThrow(() -> new IllegalArgumentException("배정 가능한 배송 기사가 없습니다."));

        deliveryAdmin.updateDeliveryPossibility(DeliveryPossibility.IMPOSSIBLE);

        User user = userRepository.findById(deliveryAdmin.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));

        return new AssignDeliveryAdminResponseDtoV1(deliveryAdmin.getUserId(), user.getUsername());
    }


}
