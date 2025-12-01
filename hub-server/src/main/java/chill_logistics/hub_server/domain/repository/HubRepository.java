package chill_logistics.hub_server.domain.repository;

import chill_logistics.hub_server.domain.entity.Hub;
import java.util.List;

public interface HubRepository {

    boolean findByName(String name);

    void save(Hub hub);

    List<Hub> findAll(int page, int size);

}
