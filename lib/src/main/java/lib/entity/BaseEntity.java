package lib.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(updatable = false)
    protected UUID createdBy;

    @LastModifiedBy
    protected UUID updatedBy;

    protected UUID deletedBy;

    protected LocalDateTime deletedAt;

    public void delete(UUID userId) {
        this.deletedBy = userId;
        this.deletedAt = LocalDateTime.now();
    }

    public void recover() {
        this.deletedBy = null;
        this.deletedAt = null;
    }
}