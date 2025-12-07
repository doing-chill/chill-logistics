package chill_logistics.hub_server.application;


import chill_logistics.hub_server.application.route.HubRouteEdge;
import chill_logistics.hub_server.application.route.HubRouteNode;
import chill_logistics.hub_server.application.service.HubEdgeWeightProvider;
import chill_logistics.hub_server.application.vo.EdgeWeight;
import chill_logistics.hub_server.application.vo.HubRouteResult;
import chill_logistics.hub_server.domain.entity.HubInfo;
import chill_logistics.hub_server.domain.entity.HubRouteLog;
import chill_logistics.hub_server.domain.entity.HubRouteLogStop;
import chill_logistics.hub_server.domain.repository.HubInfoRepository;
import chill_logistics.hub_server.domain.repository.HubRouteLogRepository;
import chill_logistics.hub_server.domain.repository.HubRouteLogStopRepository;
import chill_logistics.hub_server.lib.error.ErrorCode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;
import lib.web.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubRouteService {

    private final HubEdgeWeightProvider hubEdgeWeightProvider;
    private final HubInfoRepository hubInfoRepository;
    private final HubRouteLogRepository hubRouteLogRepository;
    private final HubRouteLogStopRepository hubRouteLogStopRepository;


    /**
     * 최단 시간 기준 경로 탐색 + 로그 저장
     */
    @Transactional
    public HubRouteResult findFastestRouteAndLog(UUID startHubId, UUID endHubId) {

        HubRouteResult result = findFastestRoute(startHubId, endHubId);
        logRoute(result);

        return result;
    }


    /**
     * 최단 시간 기준 경로 탐색 (Dijkstra)
     */
    @Transactional(readOnly = true)
    public HubRouteResult findFastestRoute(UUID startHubId, UUID endHubId) {

        List<HubInfo> connections = hubInfoRepository.findAll();

        if (connections.isEmpty()) {
            log.warn("[HubRoute] no hub connections in DB");
            throw new BusinessException(ErrorCode.HUB_ROUTE_NOT_FOUND);
        }


        // 1. 인접 리스트 그래프 구성 (duration 기준 weight)
        Map<UUID, List<HubRouteEdge>> graph = new HashMap<>();

        for (HubInfo info : connections) {
            UUID from = info.getStartHubId();
            UUID to = info.getEndHubId();

            // computeInfAbsent -> Map에서 key가 없을 때만 value를 생성해서 넣어주는 메서드
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new HubRouteEdge(to, info));

        }

        // 2. 다익스트라 실행
        return dijkstraByDuration(graph, startHubId, endHubId);
    }

    private HubRouteResult dijkstraByDuration(Map<UUID, List<HubRouteEdge>> graph, UUID startHubId, UUID endHubId) {

        Map<UUID, Integer> dist = new HashMap<>();  // 최소 시간
        Map<UUID, UUID> prev = new HashMap<>();     // 경로 복원용

        // 우선순위 큐로 Node 객체를 비교할 때 n.time (노드까지의 누적 시간)을 기준으로 정렬해서 넣음
        PriorityQueue<HubRouteNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.getTime()));

        // 시작 허브의 거리를 0으로 두고, 나머지는 모두 무한대(최대값)로 초기화
        for (UUID hubId : graph.keySet()) {
            dist.put(hubId, Integer.MAX_VALUE);
        }

        // dist는 지금까지 발견된 최소 누적 시간으로 기록 저장소
        // 출발 시점은 0으로
        dist.put(startHubId, 0);

        // pq는 앞으로 탐색할 후보 노드 목록
        // 시작 허브를 시간 0으로 큐에 넣어서 다익스트라 탐색 시작
        pq.offer(new HubRouteNode(startHubId, 0));


        // 가장 누적 시간이 짧은 허브부터 꺼내서, 허브에서 갈 수 있는 모든 인접 허브의 거리를 갱신
        while (!pq.isEmpty()) {
            HubRouteNode cur = pq.poll();
            UUID curId = cur.getHubId();


            // 최단 거리 알고리즘의 특성상, 목적지 허브가 PQ에서 꺼내졌다는 것은 그 시간 값이 최종 최소 시간이라는 의미
            if (curId.equals(endHubId)) {
                break;
            }

            // cur.time > dist.get(curId) 이면, 이미 더 짧은 경로가 존재하는 상태라서 스킵
            // 예: 더 긴 경로로 먼저 들어갔다가, 나중에 더 짧은 경로가 발견된 경우로
            // “현재 꺼낸 값이 dist 테이블에 저장된 최소 시간보다 크면 버림
            if (cur.getTime() > dist.get(curId)) {
                continue;
            }


            // 현재 허브(curId)가 연결된 허브들의 목록을 가져온다. 없으면 빈 리스트 반환 (NPE 방지)
            // key: 현재 허브 ID,  value: 그 허브에서 갈 수 있는 인접 허브들로의 간선 목록
            List<HubRouteEdge> edges = graph.getOrDefault(curId, List.of());

            // 현재 허브에서 직접 갈 수 있는 허브들만 순회
            for (HubRouteEdge e : edges) {

                //인접 허브들까지 얼마나 걸리는지 시간 계산
                EdgeWeight weight = hubEdgeWeightProvider.getWeight(curId, e.getToHubId());

                // cur.time = 출발 → e. 현재 허브까지 걸린 누적 시간
                int nextTime = cur.getTime() + weight.durationSec();

                // 아직 도달한 적 없는 허브라면 → 현재 값 = ∞ 이고 nextTime이 기존 값보다 더 빠르면 저장
                // 목적지에 처음 도달했다고 끝나는 게 아니라 더 빠른 경로가 발견될 때까지 반복문 동안 dist를 계속 비교·갱신
                if (nextTime < dist.getOrDefault(e.getToHubId(), Integer.MAX_VALUE)) {
                    dist.put(e.getToHubId(), nextTime);

                    //prev.put(e.toHubId, curId); 를 통해 경로 추적용 역방향 링크를 기록
                    prev.put(e.getToHubId(), curId);
                    pq.offer(new HubRouteNode(e.getToHubId(), nextTime));
                }
            }
            // dist에는 반복적으로 더해진 시간 nextTime가 들어감
        }


        // 도착 허브가 여전히 무한대라면, 실제 경로가 없다고 판단하고 예외처리
        if (!dist.containsKey(endHubId) || dist.get(endHubId) == Integer.MAX_VALUE) {
            log.warn("[HubRoute] no path found: {} -> {}", startHubId, endHubId);
            throw new BusinessException(ErrorCode.HUB_ROUTE_NOT_FOUND);
        }


        // 3. 경로 복원
        // prev 맵을 이용해 endHubId → ... → startHubId 방향으로 역추적을 진행
        // 리스트에 담고 나서 Collections.reverse(path)로 순서를 뒤집어 최종적으로 start → ... → end 순서의 경로를 얻음
        List<UUID> path = new ArrayList<>();
        UUID cur = endHubId;
        while (cur != null) {
            path.add(cur);
            cur = prev.get(cur);
        }
        Collections.reverse(path);

        // 4. 총 거리 합산
        // path 리스트를 인접한 두 개씩 (from, to) 쌍으로 묶어서 순회
        BigDecimal totalDistance = BigDecimal.ZERO;

        for (int i = 0; i < path.size() - 1; i++) {
            UUID from = path.get(i);
            UUID to = path.get(i + 1);

            // 그래프에서 from 의 인접 간선 목록을 가져와 to 로 가는 간선을 찾음
            List<HubRouteEdge> edges = graph.getOrDefault(from, List.of());
            HubRouteEdge edge = edges.stream()
                .filter(e -> e.getToHubId().equals(to))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_EDGE_NOT_FOUND));


            // 거리 합산은 다시 weight를 조회해서 distanceKm만 사용했다.
            // hubEdgeWeightProvider 내부 캐시 덕분에 Kakao API는 필요 시에만 호출되었다.
            EdgeWeight weight = hubEdgeWeightProvider.getWeight(from, to);
            // 해당 간선의 distanceKm 를 모두 더해 총 이동 거리를 계산
            totalDistance = totalDistance.add(weight.distanceKm());
        }

        int totalDuration = dist.get(endHubId);

        return new HubRouteResult(
            startHubId,
            endHubId,
            path,
            totalDuration,
            totalDistance
        );
    }

    /**
     * 경로 로그 테이블에 저장
     */
    @Transactional
    public void logRoute(HubRouteResult result) {

        HubRouteLog logEntity = HubRouteLog.create(
            result.startHubId(),
            result.endHubId(),
            result.totalDurationSec(),
            result.totalDistanceKm()
        );
        hubRouteLogRepository.save(logEntity);

        int seq = 0;
        for (UUID hubId : result.pathHubIds()) {
            HubRouteLogStop stop = HubRouteLogStop.create(
                logEntity.getId(),
                hubId,
                seq++
            );
            hubRouteLogStopRepository.save(stop);
        }

        log.info("[HubRoute] Logged route {} -> {} : {} sec, {} km, {} hops",
            result.startHubId(), result.endHubId(),
            result.totalDurationSec(), result.totalDistanceKm(),
            result.pathHubIds().size());
    }


}
