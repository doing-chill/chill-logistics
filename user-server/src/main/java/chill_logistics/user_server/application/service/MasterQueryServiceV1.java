package chill_logistics.user_server.application.service;

import chill_logistics.user_server.application.dto.query.MasterSignupUserQueryV1;
import chill_logistics.user_server.application.dto.query.MasterUserInfoListQueryV1;
import chill_logistics.user_server.domain.entity.DeliveryAdmin;
import chill_logistics.user_server.domain.entity.User;
import chill_logistics.user_server.domain.port.HubPort;
import chill_logistics.user_server.domain.repository.DeliveryAdminRepository;
import chill_logistics.user_server.domain.repository.UserRepository;
import chill_logistics.user_server.infrastructure.hub.HubFeignClientV1;
import chill_logistics.user_server.infrastructure.hub.HubInfoResponseV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MasterQueryServiceV1 {

    private final UserRepository userRepository;
    private final DeliveryAdminRepository deliveryAdminRepository;
    private final HubFeignClientV1 hubClientV1;
    private final HubPort hubPort;

    @Transactional(readOnly = true)
    public List<MasterSignupUserQueryV1> readSignupUserList() {

        List<User> users = userRepository.findSignupUsers();

        List<MasterSignupUserQueryV1> resultList = users.stream()
                .map(user -> {

                    UUID hubId = deliveryAdminRepository.findByUserId(user.getId())
                            .map(DeliveryAdmin::getHubId)
                            .orElse(null);

                    String hubName = null;

                    if(hubId != null) {
                        try {
                            hubName = hubPort.readHubName(hubId);
                        } catch (Exception e) {
                            log.warn("허브 정보 조회 실패. userId={}, hubId={}", user.getId(), hubId);
                        }
                    }

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

    @Transactional(readOnly = true)
    public List<MasterUserInfoListQueryV1> readUserInfoList() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {

                    UUID hubId = deliveryAdminRepository.findByUserId(user.getId())
                            .map(DeliveryAdmin::getHubId)
                            .orElse(null);

                    String hubName = null;

                    if(hubId != null) {
                        try {
                            hubName = hubPort.readHubName(hubId);
                        } catch (Exception e) {
                            log.warn("허브 정보 조회 실패. userId={}, hubId={}", user.getId(), hubId);
                        }
                    }

                    return MasterUserInfoListQueryV1.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .username(user.getUsername())
                            .nickname(user.getNickname())
                            .role(user.getRole())
                            .hubId(hubId)
                            .hubName(hubName)
                            .build();
                })
                .toList();
    }
}
