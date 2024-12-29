package ma.m3achaba.plantes.services.imp;


import jakarta.persistence.EntityNotFoundException;
import ma.m3achaba.plantes.common.PageResponse;
import ma.m3achaba.plantes.dto.ArticleRequest;
import ma.m3achaba.plantes.dto.ArticleResponse;
import ma.m3achaba.plantes.mapper.ArticleMapper;
import ma.m3achaba.plantes.model.Article;
import ma.m3achaba.plantes.model.Plantes;
import ma.m3achaba.plantes.repo.ArticleRepository;
import ma.m3achaba.plantes.repo.PlantesRepository;
import ma.m3achaba.plantes.util.images.ImgService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleMapper articleMapper;

    @Mock
    private ImgService imgService;

    @Mock
    private PlantesRepository plantesRepository;

    @InjectMocks
    private ArticleService articleService;

    private ArticleRequest articleRequest;
    private Article article;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mocking MultipartFile
        List<Plantes> plantesList = new ArrayList<>();
        plantesList.add(new Plantes());
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);

        // Initialize mock entities
        articleRequest = new ArticleRequest("Test Title", "Test Content", List.of(1), mockFile); // Include mock MultipartFile
        article = new Article();
        article.setId(1L);
        article.setTitle("Test Title");
        article.setContent("Test Content");
        article.setImage("path/to/image");
        article.setPlantes(plantesList);  // Initialize the plantes list to avoid NPE
    }


    @Test
    void save_ValidRequest_ReturnsArticleResponse() {
        // Arrange
        Article savedArticle = new Article();
        savedArticle.setId(1L);
        savedArticle.setTitle("Test Title");
        savedArticle.setContent("Test Content");
        savedArticle.setImage("path/to/image");

        when(articleRepository.existsByTitleContainingIgnoreCase("Test Title")).thenReturn(false);
        when(articleMapper.toEntity(articleRequest)).thenReturn(article);
        when(imgService.addImage(any(), any())).thenReturn("path/to/image");
        when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);
        when(articleMapper.toResponse(savedArticle)).thenReturn(new ArticleResponse(1L, "Test Title", "Test Content", "path/to/image", "2024-12-29", List.of(1L)));

        // Act
        Optional<ArticleResponse> result = articleService.save(articleRequest);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Title", result.get().getTitle());
        assertEquals("Test Content", result.get().getContent());
        assertEquals("path/to/image", result.get().getImage());
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void save_ArticleAlreadyExists_ThrowsEntityNotFoundException() {
        // Arrange
        when(articleRepository.existsByTitleContainingIgnoreCase("Test Title")).thenReturn(true);

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            articleService.save(articleRequest);
        });
        assertEquals("Article with title 'Test Title' already exists", thrown.getMessage());
    }

    @Test
    void update_ValidRequest_ReturnsUpdatedArticleResponse() {
        // Arrange
        String newTitle = "Updated Title";
        String newContent = "Updated Content";
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);

        ArticleRequest updatedArticleRequest = new ArticleRequest(newTitle, newContent, List.of(1), mockFile);

        Article existingArticle = new Article();
        existingArticle.setId(1L);
        existingArticle.setTitle("Old Title");
        existingArticle.setContent("Old Content");
        existingArticle.setImage("path/to/oldImage");

        // Mock the findById to return an article
        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(imgService.addImage(any(), any())).thenReturn("path/to/newImage");
        when(articleRepository.save(any(Article.class))).thenReturn(existingArticle);  // Ensure the article is saved
        when(articleMapper.toResponse(any(Article.class)))
                .thenReturn(new ArticleResponse(1L, newTitle, newContent, "path/to/newImage", "2024-12-29", List.of(1L)));

        // Act
        Optional<ArticleResponse> result = articleService.update(updatedArticleRequest, 1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(newTitle, result.get().getTitle());
        assertEquals(newContent, result.get().getContent());
    }

    @Test
    void update_ArticleNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            articleService.update(articleRequest, 1L);
        });
        assertEquals("Article 1 not found", thrown.getMessage());
    }

    @Test
    void delete_ValidRequest_ReturnsDeletedArticleResponse() {
        // Arrange
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(articleMapper.toResponse(article)).thenReturn(new ArticleResponse(1L, "Test Title", "Test Content", "path/to/image", "2024-12-29", List.of(1L)));

        // Act
        Optional<ArticleResponse> result = articleService.delete(1L);

        // Assert
        assertTrue(result.isPresent());
        verify(articleRepository, times(1)).delete(article);
    }

    @Test
    void delete_ArticleNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            articleService.delete(1L);
        });
        assertEquals("Article 1 not found", thrown.getMessage());
    }

    @Test
    void findById_ValidId_ReturnsArticleResponse() {
        // Arrange
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(articleMapper.toResponse(article)).thenReturn(new ArticleResponse(1L, "Test Title", "Test Content", "path/to/image", "2024-12-29", List.of(1L)));

        // Act
        Optional<ArticleResponse> result = articleService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Title", result.get().getTitle());
        assertEquals("Test Content", result.get().getContent());
    }

    @Test
    void findById_ArticleNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            articleService.findById(1L);
        });
        assertEquals("Article 1 not found", thrown.getMessage());
    }

    @Test
    void findAll_ValidRequest_ReturnsPageOfArticleResponse() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Article> page = mock(Page.class);
        Article article1 = new Article();
        article1.setId(1L);
        article1.setTitle("Test Title");
        article1.setContent("Test Content");
        article1.setImage("path/to/image");

        when(page.getContent()).thenReturn(List.of(article1)); // Mock list with a valid article
        when(page.getTotalElements()).thenReturn(1L);
        when(page.getTotalPages()).thenReturn(1);
        when(page.isFirst()).thenReturn(true);
        when(page.isLast()).thenReturn(true);
        when(articleRepository.findAll(pageRequest)).thenReturn(page);
        when(articleMapper.toResponse(any(Article.class)))
                .thenReturn(new ArticleResponse(1L, "Test Title", "Test Content", "path/to/image", "2024-12-29", List.of(1L)));

        // Act
        var result = articleService.findAll(0, 10);

        // Assert
        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals("Test Title", result.getContent().get(0).getTitle());
    }


    @Test
    void findAllWithSearch_ValidRequest_ReturnsPageOfArticleResponse() {
        // Arrange
        String searchTerm = "Test Title";
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Article> page = mock(Page.class);
        when(page.getContent()).thenReturn(List.of(article)); // Mock list of articles, not null
        when(page.getTotalElements()).thenReturn(1L);
        when(page.getTotalPages()).thenReturn(1);
        when(page.isFirst()).thenReturn(true);
        when(page.isLast()).thenReturn(true);
        when(articleRepository.findByTitleContainingIgnoreCase(searchTerm, pageRequest)).thenReturn(page);
        when(articleMapper.toResponse(any(Article.class))).thenReturn(new ArticleResponse(1L, "Test Title", "Test Content", "path/to/image", "2024-12-29", List.of(1L)));

        // Act
        var result = articleService.findAllWithSearch(0, 10, searchTerm);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Test Title", result.getContent().get(0).getTitle());
    }
    @Test
    void findAll_ReturnsListOfArticleResponse() {
        // Arrange
        Article article1 = new Article();
        article1.setId(1L);
        article1.setTitle("Test Title");
        article1.setContent("Test Content");
        article1.setImage("path/to/image");

        List<Article> articles = List.of(article1);

        when(articleRepository.findAll()).thenReturn(articles);
        when(articleMapper.toResponse(any(Article.class)))
                .thenReturn(new ArticleResponse(1L, "Test Title", "Test Content", "path/to/image", "2024-12-29", List.of(1L)));

        // Act
        List<ArticleResponse> result = articleService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).getTitle());
        assertEquals("Test Content", result.get(0).getContent());
        assertEquals("path/to/image", result.get(0).getImage());
    }
    @Test
    void findAllWithSearch_ValidSearchTerm_ReturnsListOfArticleResponse() {
        // Arrange
        String searchTerm = "Test Title";
        Article article1 = new Article();
        article1.setId(1L);
        article1.setTitle("Test Title");
        article1.setContent("Test Content");
        article1.setImage("path/to/image");

        List<Article> articles = List.of(article1);

        when(articleRepository.findByTitleContainingIgnoreCase(searchTerm)).thenReturn(articles);
        when(articleMapper.toResponse(any(Article.class)))
                .thenReturn(new ArticleResponse(1L, "Test Title", "Test Content", "path/to/image", "2024-12-29", List.of(1L)));

        // Act
        List<ArticleResponse> result = articleService.findAllWithSearch(searchTerm);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).getTitle());
        assertEquals("Test Content", result.get(0).getContent());
        assertEquals("path/to/image", result.get(0).getImage());
    }
    @Test
    void findAllByNameMaladie_ValidMaladieName_ReturnsPageResponse() {
        // Arrange
        String maladieName = "Maladie A";
        int page = 0;
        int size = 10;

        Plantes plantes = new Plantes();
        plantes.setName("Maladie A");

        Article article1 = new Article();
        article1.setId(1L);
        article1.setTitle("Test Title");
        article1.setContent("Test Content");
        article1.setImage("path/to/image");

        Page<Article> pageResult = mock(Page.class);
        when(pageResult.getContent()).thenReturn(List.of(article1));
        when(pageResult.getTotalElements()).thenReturn(1L);
        when(pageResult.getTotalPages()).thenReturn(1);

        when(plantesRepository.findAllByNameContainingIgnoreCase(maladieName)).thenReturn(List.of(plantes));
        when(articleRepository.findAllByPlantes(plantes, PageRequest.of(page, size))).thenReturn(pageResult);
        when(articleMapper.toResponse(any(Article.class)))
                .thenReturn(new ArticleResponse(1L, "Test Title", "Test Content", "path/to/image", "2024-12-29", List.of(1L)));

        // Act
        PageResponse<ArticleResponse> result = articleService.findAllByMaladieName(page, size, maladieName);

        // Assert
        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(1, result.getContent().size());
        assertEquals("Test Title", result.getContent().get(0).getTitle());
    }

    @Test
    void findAllByNameMaladie_MaladieNotFound_ReturnsEmptyPageResponse() {
        // Arrange
        String maladieName = "Maladie X";
        int page = 0;
        int size = 10;

        when(plantesRepository.findAllByNameContainingIgnoreCase(maladieName)).thenReturn(List.of());

        // Act
        PageResponse<ArticleResponse> result = articleService.findAllByMaladieName(page, size, maladieName);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }


}
