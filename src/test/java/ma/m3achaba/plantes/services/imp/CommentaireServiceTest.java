package ma.m3achaba.plantes.services.imp;

import jakarta.persistence.EntityNotFoundException;
import ma.m3achaba.plantes.common.PageResponse;
import ma.m3achaba.plantes.dto.CommentaireRequest;
import ma.m3achaba.plantes.dto.CommentaireResponse;
import ma.m3achaba.plantes.mapper.CommentaireMapper;
import ma.m3achaba.plantes.model.*;
import ma.m3achaba.plantes.repo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentaireServiceTest {

    @Mock
    private CommentairepltRepository commentairepltRepository;

    @Mock
    private CommentaireMapper commentaireMapper;

    @Mock
    private PlantesRepository plantesRepository;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CommentaireartRepository commentaireartRepository;

    @InjectMocks
    private CommentaireService commentaireService;

    private Plantes plante;
    private Article article;
    private User user;
    private CommentaireRequest commentaireRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock entities
        plante = new Plantes();
        plante.setId(1L);
        article = new Article();
        article.setId(1L);
        user = new User();
        user.setId(1L);
        commentaireRequest = new CommentaireRequest("Test Comment");
    }

    @Test
    void savePlante_ValidRequest_ReturnsCommentaireResponse() {
        // Arrange
        PlantComment plantComment = new PlantComment();
        plantComment.setPlantes(plante);
        plantComment.setUtilisateur(user);

        CommentaireResponse commentaireResponse = new CommentaireResponse(1L, "Test Comment", "2024-12-29", "Test User");

        when(plantesRepository.findById(1L)).thenReturn(Optional.of(plante));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(commentaireMapper.toEntityPlante(commentaireRequest)).thenReturn(plantComment);
        when(commentaireMapper.toResponse(any(PlantComment.class))).thenReturn(commentaireResponse);
        when(commentairepltRepository.save(any(PlantComment.class))).thenReturn(plantComment);

        // Act
        Optional<CommentaireResponse> result = commentaireService.savePlante(commentaireRequest, 1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Comment", result.get().getCommentaire());
        verify(commentairepltRepository, times(1)).save(any(PlantComment.class));
    }

    @Test
    void savePlante_PlanteNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        when(plantesRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            commentaireService.savePlante(commentaireRequest, 1L);
        });
        assertEquals("Plante 1 not found", thrown.getMessage());
    }

    @Test
    void listPlante_ValidRequest_ReturnsPageOfCommentaireResponse() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        PlantComment comment1 = new PlantComment();
        comment1.setMessage("Test Comment");
        comment1.setPlantes(plante);
        comment1.setUtilisateur(user);
        Page<PlantComment> page = new PageImpl<>(List.of(comment1), pageRequest, 1);

        when(plantesRepository.findById(1L)).thenReturn(Optional.of(plante));
        when(commentairepltRepository.findAllByPlantes(plante, pageRequest)).thenReturn(page);
        when(commentaireMapper.toResponse(any(PlantComment.class))).thenReturn(new CommentaireResponse(1L, "Test Comment", "2024-12-29", "Test User"));

        // Act
        PageResponse<CommentaireResponse> result = commentaireService.listPlante(1L, 0, 10);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals("Test Comment", result.getContent().get(0).getCommentaire());
    }

    @Test
    void listPlante_PlanteNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        when(plantesRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            commentaireService.listPlante(1L, 0, 10);
        });
        assertEquals("Plante 1 not found", thrown.getMessage());
    }

    @Test
    void saveArticle_ValidRequest_ReturnsCommentaireResponse() {
        // Arrange
        ArticleComment articleComment = new ArticleComment();
        articleComment.setArticle(article);
        articleComment.setUtilisateur(user);

        CommentaireResponse commentaireResponse = new CommentaireResponse(1L, "Test Article Comment", "2024-12-29", "Test User");

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(commentaireMapper.toEntityArticle(commentaireRequest)).thenReturn(articleComment);
        when(commentaireMapper.toResponse(any(ArticleComment.class))).thenReturn(commentaireResponse);
        when(commentaireartRepository.save(any(ArticleComment.class))).thenReturn(articleComment);

        // Act
        Optional<CommentaireResponse> result = commentaireService.saveArticle(commentaireRequest, 1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Article Comment", result.get().getCommentaire());
        verify(commentaireartRepository, times(1)).save(any(ArticleComment.class));
    }

    @Test
    void saveArticle_ArticleNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            commentaireService.saveArticle(commentaireRequest, 1L);
        });
        assertEquals("Article 1 not found", thrown.getMessage());
    }

    @Test
    void listArticle_ValidRequest_ReturnsPageOfCommentaireResponse() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        ArticleComment comment1 = new ArticleComment();
        comment1.setMessage("Test Article Comment");
        comment1.setArticle(article);
        comment1.setUtilisateur(user);
        Page<ArticleComment> page = new PageImpl<>(List.of(comment1), pageRequest, 1);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(commentaireartRepository.findAllByArticle(article, pageRequest)).thenReturn(page);
        when(commentaireMapper.toResponse(any(ArticleComment.class))).thenReturn(new CommentaireResponse(1L, "Test Article Comment", "2024-12-29", "Test User"));

        // Act
        PageResponse<CommentaireResponse> result = commentaireService.listArticle(1L, 0, 10);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals("Test Article Comment", result.getContent().get(0).getCommentaire());
    }

    @Test
    void listArticle_ArticleNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            commentaireService.listArticle(1L, 0, 10);
        });
        assertEquals("Article 1 not found", thrown.getMessage());
    }
}
