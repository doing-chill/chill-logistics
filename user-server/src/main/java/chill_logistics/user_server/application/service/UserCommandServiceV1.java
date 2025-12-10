package chill_logistics.user_server.application.service;

import chill_logistics.user_server.application.dto.command.UpdateUserInfoCommandV1;
import chill_logistics.user_server.domain.entity.User;
import chill_logistics.user_server.domain.repository.UserRepository;
import lib.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommandServiceV1 {

    private final UserRepository userRepository;

    @Transactional
    public void updateUserInfo(UpdateUserInfoCommandV1 command) {

        UUID userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 없습니다."));

        user.updateUserInfo(
                command.email(),
                command.username(),
                command.nickname()
        );
    }
}
