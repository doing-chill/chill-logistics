package chill_logistics.firm_server.infrastructure.repository;

import chill_logistics.firm_server.domain.entity.Firm;
import chill_logistics.firm_server.domain.repository.FirmRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;

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

}
