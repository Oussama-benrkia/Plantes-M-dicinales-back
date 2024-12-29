package ma.m3achaba.plantes.dto;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentaireRequestTest {

    @Test
    void testCommentaireRequest() {
        // Arrange
        String commentaire = "This is a comment";

        // Act
        CommentaireRequest commentaireRequest = new CommentaireRequest(commentaire);

        // Assert
        assertNotNull(commentaireRequest);
        assertEquals(commentaire, commentaireRequest.commentaire());
    }

    @Test
    void testCommentaireRequestEqualsAndHashCode() {
        // Arrange
        String commentaire = "This is a comment";
        CommentaireRequest commentaireRequest1 = new CommentaireRequest(commentaire);
        CommentaireRequest commentaireRequest2 = new CommentaireRequest(commentaire);

        // Act & Assert
        assertEquals(commentaireRequest1, commentaireRequest2); // test equals
        assertEquals(commentaireRequest1.hashCode(), commentaireRequest2.hashCode()); // test hashCode
    }

    @Test
    void testToString() {
        // Arrange
        String commentaire = "This is a comment";
        CommentaireRequest commentaireRequest = new CommentaireRequest(commentaire);

        // Act
        String toString = commentaireRequest.toString();

        // Assert
        assertTrue(toString.contains(commentaire)); // The toString should include the commentaire value
    }
}
