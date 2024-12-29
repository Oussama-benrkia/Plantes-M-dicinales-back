package ma.m3achaba.plantes.dto;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArticleRequestTest {

    @Test
    void testArticleRequestConstructorAndGetters() {
        // Arrange
        String titre = "Article Title";
        String content = "This is the content of the article";
        List<Integer> plantes = List.of(1, 2, 3); // A valid list of plant IDs
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[10]);

        // Act
        ArticleRequest articleRequest = new ArticleRequest(titre, content, plantes, image);

        // Assert
        assertNotNull(articleRequest, "ArticleRequest should not be null");
        assertEquals(titre, articleRequest.titre(), "The titre should match the input value");
        assertEquals(content, articleRequest.content(), "The content should match the input value");
        assertEquals(plantes, articleRequest.Plante(), "The plantes list should match the input value");
        assertEquals(image, articleRequest.images(), "The image file should match the input value");
    }

    @Test
    void testArticleRequestEquality() {
        // Arrange
        String titre = "Article Title";
        String content = "This is the content of the article";
        List<Integer> plantes = List.of(1, 2, 3);
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[10]);

        ArticleRequest articleRequest1 = new ArticleRequest(titre, content, plantes, image);
        ArticleRequest articleRequest2 = new ArticleRequest(titre, content, plantes, image);

        // Act & Assert
        assertEquals(articleRequest1, articleRequest2, "Two ArticleRequest objects with the same values should be equal");
        assertEquals(articleRequest1.hashCode(), articleRequest2.hashCode(), "Two ArticleRequest objects with the same values should have the same hash code");
    }




    @Test
    void testArticleRequestConstructorWithEmptyValues() {
        // Arrange
        String titre = "";
        String content = "";
        List<Integer> plantes = List.of(); // Empty list
        MockMultipartFile image = null; // No image file

        // Act
        ArticleRequest articleRequest = new ArticleRequest(titre, content, plantes, image);

        // Assert
        assertNotNull(articleRequest, "ArticleRequest should not be null even with empty fields");
        assertEquals(titre, articleRequest.titre(), "The titre should be an empty string");
        assertEquals(content, articleRequest.content(), "The content should be an empty string");
        assertEquals(plantes, articleRequest.Plante(), "The plantes list should be empty");
        assertNull(articleRequest.images(), "The image should be null if not provided");
    }
}
