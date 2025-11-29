package lib.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false, columnDefinition = "BINARY(16)", nullable = false)
    protected UUID createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", columnDefinition = "BINARY(16)", nullable = false)
    protected UUID updatedBy;

    @Column(name = "deleted_by", columnDefinition = "BINARY(16)")
    protected UUID deletedBy;

    @Column(name = "deleted_at")
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