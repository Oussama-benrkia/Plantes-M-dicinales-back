package ma.m3achaba.plantes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.m3achaba.plantes.common.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // Stratégie TABLE_PER_CLASS
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Remplacez IDENTITY par SEQUENCE
    private Long id;
    private String message;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
}
