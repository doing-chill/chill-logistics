package chill_logistics.firm_server.infrastructure.repository;

import chill_logistics.firm_server.domain.entity.Firm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaFirmRepository extends JpaRepository<Firm, UUID> {
}
