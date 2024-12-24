package ma.m3achaba.plantes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.m3achaba.plantes.common.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // ou TABLE_PER_CLASS si vous voulez des tables séparées
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class Commentaire extends BaseEntity {
    private String message;
}

