package chill_logistics.user_server.application.service;

import chill_logistics.user_server.application.dto.command.MasterUpdateSignupStatusCommandV1;
import chill_logistics.user_server.application.dto.command.MasterUpdateUserInfoCommandV1;
import chill_logistics.user_server.domain.entity.User;
import chill_logistics.user_server.domain.repository.DeliveryAdminRepository;
import chill_logistics.user_server.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MasterServiceV1 {

    private final UserRepository userRepository;
    private final DeliveryAdminRepository deliveryAdminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updateUserInfo(MasterUpdateUserInfoCommandV1 command) {

        log.info("[MasterServiceV1] ===== updateUserInfo START =====");
        log.info("[MasterServiceV1] 요청 Command - userId={}, email={}, username={}, nickname={}, role={}, hubId={}",
                command.userId(),
                command.email(),
                command.username(),
                command.nickname(),
                command.role(),
                command.hubId()
        );

        try {
            log.info("[MasterServiceV1] 1. User 조회 시도 - userId={}", command.userId());

            User user = userRepository.findById(command.userId())
                    .orElseThrow(() -> {
                        log.warn("[MasterServiceV1] 1-1. User 조회 실패 - userId={}", command.userId());
                        return new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + command.userId());
                    });

            log.info("[MasterServiceV1] 1-2. User 조회 성공 - userId={}, 현재 email={}, 현재 role={}",
                    user.getId(),
                    user.getEmail(),
                    user.getRole()
            );

            // 비밀번호 인코딩
            log.info("[MasterServiceV1] 2. 비밀번호 인코딩 시작 - 원본 비밀번호 길이={}",
                    command.password() != null ? command.password().length() : null);

            String encodedPassword = passwordEncoder.encode(command.password());

            log.info("[MasterServiceV1] 2-1. 비밀번호 인코딩 완료 - encodedPassword 길이={}",
                    encodedPassword != null ? encodedPassword.length() : null);

            // User 업데이트
            log.info("[MasterServiceV1] 3. User 정보 업데이트 시작 - userId={}", user.getId());

            user.updateInfoFromMaster(
                    command.email(),
                    command.username(),
                    command.nickname(),
                    encodedPassword,
                    command.role()
            );

            log.info("[MasterServiceV1] 3-1. User 업데이트 완료 - 변경 email={}, username={}, nickname={}, role={}",
                    user.getEmail(),
                    user.getUsername(),
                    user.getNickname(),
                    user.getRole()
            );

            // DeliveryAdmin은 나중에 붙일 예정
        /*
        log.info("[MasterServiceV1] 4. DeliveryAdmin 조회 시도 - userId={}", command.userId());

        DeliveryAdmin deliveryAdmin = deliveryAdminRepository.findById(command.userId())
                .orElseThrow(() -> {
                    log.warn("[MasterServiceV1] 4-1. DeliveryAdmin 조회 실패 - userId={}", command.userId());
                    return new IllegalArgumentException("해당 사용자는 DeliveryAdmin 정보가 없습니다. id=" + command.userId());
                });

        log.info("[MasterServiceV1] 4-2. DeliveryAdmin 조회 성공 - hubId={}", deliveryAdmin.getHubId());

        deliveryAdmin.updateHubId(command.hubId());

        log.info("[MasterServiceV1] 4-3. DeliveryAdmin hubId 업데이트 완료 - newHubId={}", deliveryAdmin.getHubId());
        */

            log.info("[MasterServiceV1] ===== updateUserInfo SUCCESS - userId={} =====", user.getId());

        } catch (Exception e) {
            log.error("[MasterServiceV1] ***** updateUserInfo FAILED - userId={}, reason={} *****",
                    command.userId(),
                    e.getMessage(),
                    e);
            throw e; // 예외 다시 던져서 상위 레벨에서 처리하게
        }
    }

    @Transactional
    public void updateSignupStatus(MasterUpdateSignupStatusCommandV1 command) {

        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + command.userId()));

        user.updateSignupStatus(command.signupStatus());
    }

}
