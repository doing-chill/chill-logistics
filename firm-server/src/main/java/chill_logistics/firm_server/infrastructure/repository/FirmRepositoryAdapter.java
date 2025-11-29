package chill_logistics.firm_server.infrastructure.repository;

import chill_logistics.firm_server.domain.repository.FirmRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FirmRepositoryAdapter implements FirmRepository {

    private final JpaFirmRepository jpaFirmRepository;
}
