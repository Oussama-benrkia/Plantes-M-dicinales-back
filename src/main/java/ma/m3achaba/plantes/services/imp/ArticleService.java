package ma.m3achaba.plantes.services.imp;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.m3achaba.plantes.common.PageResponse;
import ma.m3achaba.plantes.dto.ArticleRequest;
import ma.m3achaba.plantes.dto.ArticleResponse;
import ma.m3achaba.plantes.dto.PlantesResponse;
import ma.m3achaba.plantes.mapper.ArticleMapper;
import ma.m3achaba.plantes.model.Article;
import ma.m3achaba.plantes.model.Maladies;
import ma.m3achaba.plantes.model.Plantes;
import ma.m3achaba.plantes.repo.ArticleRepository;
import ma.m3achaba.plantes.repo.PlantesRepository;
import ma.m3achaba.plantes.services.ServiceMetier;
import ma.m3achaba.plantes.util.images.ImagesFolder;
import ma.m3achaba.plantes.util.images.ImgService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService implements ServiceMetier<ArticleResponse, ArticleRequest> {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final ImgService imgService;
    private final PlantesRepository plantesRepository;

    private Article findArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article " + id + " not found"));
    }

    private PageResponse<ArticleResponse> createPageResponse(Page<Article> page) {
        List<ArticleResponse> list = page.getContent().stream()
                .map(articleMapper::toResponse)
                .toList();

        return PageResponse.<ArticleResponse>builder()
                .totalElements(page.getTotalElements())
                .number(page.getNumber())
                .last(page.isLast())
                .first(page.isFirst())
                .content(list)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public Optional<ArticleResponse> findById(Long id) {
        return Optional.ofNullable(articleMapper.toResponse(findArticleById(id)));
    }

    @Override
    public PageResponse<ArticleResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> res = articleRepository.findAll(pageable);
        return createPageResponse(res);
    }

    @Override
    public List<ArticleResponse> findAll() {
        return articleRepository.findAll().stream()
                .map(articleMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<ArticleResponse> save(ArticleRequest t) {
        if (articleRepository.existsByTitleContainingIgnoreCase(t.titre())) {
            throw new EntityNotFoundException("Article with title '" + t.titre() + "' already exists");
        }

        String path = imgService.addImage(t.images(), ImagesFolder.ARTICLE);

        Article article = articleMapper.toEntity(t);
        article.setImage(path);
        Article savedArticle = articleRepository.save(article);
        return Optional.ofNullable(articleMapper.toResponse(savedArticle));
    }

    @Override
    public Optional<ArticleResponse> update(ArticleRequest t, Long id) {
        Article article = findArticleById(id);
        boolean change = false;

        if (t.titre() != null && !t.titre().isEmpty() && !t.titre().equals(article.getTitle())) {
            article.setTitle(t.titre());
            change = true;
        }

        if (t.content() != null && !t.content().isEmpty() && !t.content().equals(article.getContent())) {
            article.setContent(t.content());
            change = true;
        }

        if (!t.images().isEmpty()) {
            imgService.deleteImage(article.getImage());
            String path = imgService.addImage(t.images(), ImagesFolder.ARTICLE);
            article.setImage(path);
            change = true;
        }

        if (change) {
            article = articleRepository.save(article);
        }

        return Optional.ofNullable(articleMapper.toResponse(article));
    }

    @Override
    public Optional<ArticleResponse> delete(Long id) {
        Article article = findArticleById(id);
        article.getPlantes().clear();
        article.getCommentaires().clear();
        if (!article.getImage().isEmpty()) {
            imgService.deleteImage(article.getImage());
        }
        articleRepository.delete(article);
        return Optional.ofNullable(articleMapper.toResponse(article));
    }

    public PageResponse<ArticleResponse> findAllWithSearch(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> res = articleRepository.findByTitleContainingIgnoreCase(search, pageable);
        return createPageResponse(res);
    }

    public List<ArticleResponse> findAllWithSearch(String search) {
        return articleRepository.findByTitleContainingIgnoreCase(search).stream()
                .map(articleMapper::toResponse)
                .toList();
    }

    public PageResponse<ArticleResponse> findallbynameMaladie(int page, int size, String maladieName) {
        Pageable pageable = PageRequest.of(page, size);
        Plantes maladies=plantesRepository.findAllByNameContainingIgnoreCase(maladieName).get(0);
        if (maladies == null) {
            createEmptyPageResponse(pageable);
        }
        return createPageResponse(articleRepository.findAllByPlantes(maladies, pageable));
    }
    private PageResponse<PlantesResponse> createEmptyPageResponse(Pageable pageable) {
        return PageResponse.<PlantesResponse>builder()
                .content(List.of()) // Empty list
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements(0)
                .totalPages(0)
                .first(true)
                .last(true)
                .build();
    }
}
