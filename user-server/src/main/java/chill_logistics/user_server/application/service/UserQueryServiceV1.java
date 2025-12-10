package chill_logistics.user_server.application.service;

import chill_logistics.user_server.application.dto.query.UserInfoQueryV1;
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
public class UserQueryServiceV1 {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfoQueryV1 readUserInfo() {

        UUID userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        return new UserInfoQueryV1(
                user.getEmail(),
                user.getUsername(),
                user.getNickname(),
                user.getRole()
        );
    }
}
