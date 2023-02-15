package uz.md.shopapp.domain.template;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class AbsLongEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedBy
    @Column(updatable = false)
    private Long addedById;

    @LastModifiedBy
    private Long updatedById;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime addedAt;

    @UpdateTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime updatedAt;

    private boolean deleted = false;

    private boolean active = true;
}
