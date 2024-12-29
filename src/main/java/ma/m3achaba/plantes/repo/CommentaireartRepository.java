package ma.m3achaba.plantes.repo;

import ma.m3achaba.plantes.model.Article;
import ma.m3achaba.plantes.model.Commentaire_article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentaireartRepository extends JpaRepository<Commentaire_article,Long> {
    Page<Commentaire_article> findAllByArticle(Article plante, Pageable pageable);

}
