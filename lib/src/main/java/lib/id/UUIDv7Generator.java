package lib.id;

import com.fasterxml.uuid.Generators;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class UUIDv7Generator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        // timeBasedEpochGenerator(): UUIDv7 구현체와 동일한 timestamp 기반 증가형 UUID
        return Generators.timeBasedEpochGenerator().generate(); // UUIDv7 생성
    }
}
