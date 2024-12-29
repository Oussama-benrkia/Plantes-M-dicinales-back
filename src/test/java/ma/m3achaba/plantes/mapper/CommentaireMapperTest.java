package ma.m3achaba.plantes.mapper;

import ma.m3achaba.plantes.dto.CommentaireRequest;
import ma.m3achaba.plantes.dto.CommentaireResponse;
import ma.m3achaba.plantes.model.ArticleComment;
import ma.m3achaba.plantes.model.PlantComment;
import ma.m3achaba.plantes.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentaireMapperTest {

    private CommentaireMapper commentaireMapper;

    @BeforeEach
    void setUp() {
        commentaireMapper = new CommentaireMapper();
    }

    @Test
    void testToEntityPlante() {
        // Arrange
        String commentaireText = "This is a comment";
        CommentaireRequest commentaireRequest = new CommentaireRequest(commentaireText);

        // Act
        PlantComment plantComment = commentaireMapper.toEntityPlante(commentaireRequest);

        // Assert
        assertNotNull(plantComment, "PlantComment should not be null");
        assertEquals(commentaireText, plantComment.getMessage(), "The message should match the commentaire text");
    }

    @Test
    void testToResponseFromPlantComment() {
        // Arrange
        String commentaireText = "This is a comment";
        LocalDateTime createdDate = LocalDateTime.now();
        User utilisateur = Mockito.mock(User.class);
        Mockito.when(utilisateur.getNom()).thenReturn("User Name");
        PlantComment plantComment = PlantComment.builder()
                .message(commentaireText)
                .createdDate(createdDate)
                .utilisateur(utilisateur)
                .id(1L)
                .build();

        // Act
        CommentaireResponse response = commentaireMapper.toResponse(plantComment);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(commentaireText, response.getCommentaire(), "The commentaire text should match");
        assertEquals("User Name", response.getName(), "The name should match");
        assertNotNull(response.getDate(), "The date should not be null");
        assertNotNull(response.getId(), "The id should not be null");
    }

    @Test
    void testToEntityArticle() {
        // Arrange
        String commentaireText = "This is a comment";
        CommentaireRequest commentaireRequest = new CommentaireRequest(commentaireText);

        // Act
        ArticleComment articleComment = commentaireMapper.toEntityArticle(commentaireRequest);

        // Assert
        assertNotNull(articleComment, "ArticleComment should not be null");
        assertEquals(commentaireText, articleComment.getMessage(), "The message should match the commentaire text");
    }

    @Test
    void testToResponseFromArticleComment() {
        // Arrange
        String commentaireText = "This is a comment";
        LocalDateTime createdDate = LocalDateTime.now();
        User utilisateur = Mockito.mock(User.class);
        Mockito.when(utilisateur.getNom()).thenReturn("User Name");
        ArticleComment articleComment = ArticleComment.builder()
                .message(commentaireText)
                .createdDate(createdDate)
                .utilisateur(utilisateur)
                .id(1L)
                .build();

        // Act
        CommentaireResponse response = commentaireMapper.toResponse(articleComment);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(commentaireText, response.getCommentaire(), "The commentaire text should match");
        assertEquals("User Name", response.getName(), "The name should match");
        assertNotNull(response.getDate(), "The date should not be null");
        assertNotNull(response.getId(), "The id should not be null");
    }
}
