package ma.m3achaba.plantes.repo;

import ma.m3achaba.plantes.model.Article;
import ma.m3achaba.plantes.model.Plantes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    List<Article> findByTitleContainingIgnoreCase(String title);
    Boolean existsByTitleContainingIgnoreCase(String title);
    Page<Article> findAllByPlantes(Plantes plantes, Pageable pageable);

}
