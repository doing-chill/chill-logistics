package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.repository.HubRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
public class HubRepositoryAdapter implements HubRepository {

    private final JpaHubRepository jpaHubRepository;

    @Override
    public boolean findByName(String name) {
        return jpaHubRepository.findByName(name);
    }

    @Override
    public void save(Hub hub) {
        jpaHubRepository.save(hub);
    }

    @Override
    public List<Hub> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return jpaHubRepository.findAll(pageRequest).getContent();
    }


}
