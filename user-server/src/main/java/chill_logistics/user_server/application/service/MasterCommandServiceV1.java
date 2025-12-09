package chill_logistics.user_server.application.service;

import chill_logistics.user_server.application.dto.command.MasterUpdateSignupStatusCommandV1;
import chill_logistics.user_server.application.dto.command.MasterUpdateUserInfoCommandV1;
import chill_logistics.user_server.domain.entity.User;
import chill_logistics.user_server.domain.repository.DeliveryAdminRepository;
import chill_logistics.user_server.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MasterCommandServiceV1 {

    private final UserRepository userRepository;
    private final DeliveryAdminRepository deliveryAdminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updateUserInfo(MasterUpdateUserInfoCommandV1 command) {

        User user = userRepository.findById(command.userId())
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + command.userId())
                );

        String encodedPassword = passwordEncoder.encode(command.password());

        user.updateInfoFromMaster(
                command.email(),
                command.username(),
                command.nickname(),
                encodedPassword,
                command.role()
        );

        // DeliveryAdmin은 추후 적용 예정
        /*
        DeliveryAdmin deliveryAdmin = deliveryAdminRepository.findById(command.userId())
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 사용자는 DeliveryAdmin 정보가 없습니다. id=" + command.userId())
                );

        deliveryAdmin.updateHubId(command.hubId());
        */
    }

    @Transactional
    public void updateSignupStatus(MasterUpdateSignupStatusCommandV1 command) {

        User user = userRepository.findById(command.userId())
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + command.userId())
                );

        user.updateSignupStatus(command.signupStatus());
    }
}
