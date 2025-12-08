package chill_logistics.user_server.application.service;

import chill_logistics.user_server.application.dto.query.MasterSignupUserQueryV1;
import chill_logistics.user_server.domain.entity.User;
import chill_logistics.user_server.domain.repository.DeliveryAdminRepository;
import chill_logistics.user_server.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MasterQueryServiceV1 {

    private final UserRepository userRepository;
    private final DeliveryAdminRepository deliveryAdminRepository;

    @Transactional(readOnly = true)
    public List<MasterSignupUserQueryV1> readSignupUserList() {

        List<User> users = userRepository.findSignupUsers();

        List<MasterSignupUserQueryV1> resultList = users.stream()
                .map(user -> {
                    // 추후에 호출해올 예정
                    String hubName = null;

                    return new MasterSignupUserQueryV1(
                            user.getId(),
                            user.getEmail(),
                            user.getUsername(),
                            user.getNickname(),
                            user.getRole(),
                            user.getSignupStatus(),
                            hubName
                    );
                })
                .toList();

        return resultList;
    }
}
