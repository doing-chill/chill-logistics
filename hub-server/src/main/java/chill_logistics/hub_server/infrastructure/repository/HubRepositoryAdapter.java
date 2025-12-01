package chill_logistics.hub_server.infrastructure.repository;

import chill_logistics.hub_server.domain.entity.Hub;
import chill_logistics.hub_server.domain.repository.HubRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class HubRepositoryAdapter implements HubRepository {

    private final JpaHubRepository jpaHubRepository;

    @Override
    public boolean existsByName(String name) {
        return jpaHubRepository.existsByName(name);
    }

    @Override
    public void save(Hub hub) {
        jpaHubRepository.save(hub);
    }

    @Override
    public List<Hub> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
        return jpaHubRepository.findAll(pageRequest).getContent();
    }

    @Override
    public boolean existsById(UUID hubId) {
        return jpaHubRepository.existsById(hubId);
    }

    @Override
    public Optional<Hub> findById(UUID hubId) {
        return jpaHubRepository.findById(hubId);
    }

    @Override
    public List<Hub> findByNameOrFullAddressContaining(String nameOrFullAddress, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return jpaHubRepository.findByNameAndFullAddressContaining(nameOrFullAddress, nameOrFullAddress, pageRequest);
    }


}
