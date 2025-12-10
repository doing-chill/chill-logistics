package chill_logistics.firm_server.infrastructure.repository;

import chill_logistics.firm_server.domain.entity.Firm;
import chill_logistics.firm_server.domain.entity.FirmType;
import chill_logistics.firm_server.domain.repository.FirmRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class FirmRepositoryAdapter implements FirmRepository {

    private final JpaFirmRepository jpaFirmRepository;

    @Override
    public void save(Firm firm) {
        jpaFirmRepository.save(firm);
    }

    @Override
    public boolean existsByNameAndDeletedAtIsNull(String name) {
        return jpaFirmRepository.existsByNameAndDeletedAtIsNull(name);
    }

    @Override
    public boolean existsByFullAddressAndDeletedAtIsNull(String fullAddress) {
        return jpaFirmRepository.existsByFullAddressAndDeletedAtIsNull(fullAddress);
    }

    @Override
    public boolean existsByLatitudeAndLongitudeAndDeletedAtIsNull(BigDecimal latitude, BigDecimal longitude) {
        return jpaFirmRepository.existsByLatitudeAndLongitudeAndDeletedAtIsNull(latitude, longitude);
    }

    @Override
    public Optional<Firm> findById(UUID firmId) {
        return jpaFirmRepository.findByIdAndDeletedAtIsNull(firmId);
    }

    @Override
    public Optional<Firm> findByIdAndFirmTypeAndDeletedAtIsNull(UUID firmId, FirmType firmType) {
        return jpaFirmRepository.findByIdAndFirmTypeAndDeletedAtIsNull(firmId, firmType);
    }

    @Override
    public List<Firm> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaFirmRepository.findAllByDeletedAtIsNull(pageable);
    }

}
