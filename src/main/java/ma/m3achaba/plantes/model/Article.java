package ma.m3achaba.plantes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.m3achaba.plantes.common.BaseEntity;

import java.util.List;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Article extends BaseEntity {
    private String title;
    @Lob
    private String content;  // Note: You might want to fix the typo "Conent" to "Content"
    private String image;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "article_plantes", // Changed hyphen to underscore
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "plantes_id")
    )
    private List<Plantes> plantes;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commentaire_article> commentaires; // Changed from 'maladies' to 'plantes'
}
