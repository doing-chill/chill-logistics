package chill_logistics.delivery_server.application;

import chill_logistics.delivery_server.application.dto.command.AssignedDeliveryPersonV1;
import chill_logistics.delivery_server.infrastructure.user.UserClient;
import chill_logistics.delivery_server.infrastructure.user.dto.UserForDeliveryResponseV1;
import chill_logistics.delivery_server.presentation.ErrorCode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryPersonAssignmentService {

    private static final String ROLE_HUB_DELIVERY_PERSON = "HUB_DELIVERY_PERSON";
    private static final String ROLE_FIRM_DELIVERY_PERSON = "FIRM_DELIVERY_PERSON";

    private final UserClient userClient;

    // 역할별 라운드 로빈 인덱스 저장 (멀티스레드 환경 대응)
    private final Map<String, AtomicLong> roundRobinIndex = new ConcurrentHashMap<>();

    // 허브 배송 담당자 배정
    public AssignedDeliveryPersonV1 assignHubDeliveryPerson() {
        return assignByRole(ROLE_HUB_DELIVERY_PERSON);
    }

    // 업체 배송 담당자 배정
    public AssignedDeliveryPersonV1 assignFirmDeliveryPerson() {
        return assignByRole(ROLE_FIRM_DELIVERY_PERSON);
    }

    private AssignedDeliveryPersonV1 assignByRole(String role) {

        // user-server에서 role 별 유저 목록 조회
        List<UserForDeliveryResponseV1> deliveryCandidates = userClient.getUsersByRole(role);

        if (deliveryCandidates == null || deliveryCandidates.isEmpty()) {
            throw new BusinessException(ErrorCode.DELIVERT_PERSON_NOT_FOUND);
        }

        // 해당 role에 대한 라운드 로빈 인덱스 가져오고, 없으면 생성
        AtomicLong counter = roundRobinIndex.computeIfAbsent(role, key -> new AtomicLong(0L));

        // 현재 인덱스를 가져오고 증가시키는 연산
        long index = counter.incrementAndGet();

        // 후보자 수로 나머지 연산하려 실제 선택 위치 계산
        int selectedIndex = (int) (index % deliveryCandidates.size());

        // 최종 선택된 담당자
        UserForDeliveryResponseV1 chosen = deliveryCandidates.get(selectedIndex);

        return new AssignedDeliveryPersonV1(
            chosen.userId(),
            chosen.userName()
        );
    }
}
