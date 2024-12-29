package ma.m3achaba.plantes.mapper;

import ma.m3achaba.plantes.dto.ArticleRequest;
import ma.m3achaba.plantes.dto.ArticleResponse;
import ma.m3achaba.plantes.model.Article;
import ma.m3achaba.plantes.model.Plantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArticleMapperTest {

    private ArticleMapper articleMapper;
    private Environment environment;

    @BeforeEach
    void setUp() {
        // Mocking the environment to return a server address and port
        environment = Mockito.mock(Environment.class);
        Mockito.when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");
        Mockito.when(environment.getProperty("server.port", "8080")).thenReturn("8080");

        articleMapper = new ArticleMapper(environment);
    }

    @Test
    void testToEntity() {
        // Arrange
        String titre = "Article Title";
        String content = "Article content";
        List<Integer> plantes = List.of(1, 2, 3);
        ArticleRequest request = new ArticleRequest(titre, content, plantes, null);

        // Act
        Article article = articleMapper.toEntity(request);

        // Assert
        assertNotNull(article, "Article should not be null");
        assertEquals(titre, article.getTitle(), "The title should match");
        assertEquals(content, article.getContent(), "The content should match");
    }

    @Test
    void testToResponseWithImage() {
        // Arrange
        Plantes plante = new Plantes();
        plante.setId(1L);

        String imageName = "image.jpg";
        String title = "Article Title";
        String content = "Article content";
        List<Plantes> plantesList = List.of( plante);
        Article article = Article.builder()
                .id(1L)
                .title(title)
                .content(content)
                .image(imageName)
                .createdDate(LocalDateTime.now())
                .plantes(plantesList)
                .build();

        // Act
        ArticleResponse response = articleMapper.toResponse(article);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(title, response.getTitle(), "The title should match");
        assertEquals(content, response.getContent(), "The content should match");
        assertTrue(response.getImage().contains("http://localhost:8080/api/image/image.jpg"), "The image URL should be correct");
        assertEquals(1, response.getPlante().size(), "The number of plantes should match");
        assertNotNull(response.getDate(), "The date should not be null");
    }

    @Test
    void testToResponseWithoutImage() {
        // Arrange
        Plantes plante = new Plantes();
        plante.setId(1L);

        String title = "Article Title";
        String content = "Article content";
        List<Plantes> plantesList = List.of( plante);
        Article article = Article.builder()
                .id(1L)
                .title(title)
                .content(content)
                .image(null)  // No image
                .createdDate(LocalDateTime.now())
                .plantes(plantesList)
                .build();

        // Act
        ArticleResponse response = articleMapper.toResponse(article);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(title, response.getTitle(), "The title should match");
        assertEquals(content, response.getContent(), "The content should match");
        assertEquals("", response.getImage(), "The image URL should be empty");
        assertEquals(1, response.getPlante().size(), "The number of plantes should match");
        assertNotNull(response.getDate(), "The date should not be null");
    }
}
