package ma.m3achaba.plantes.repo;

import ma.m3achaba.plantes.model.Article;
import ma.m3achaba.plantes.model.ArticleComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentaireartRepository extends JpaRepository<ArticleComment ,Long> {
    Page<ArticleComment> findAllByArticle(Article plante, Pageable pageable);

}
