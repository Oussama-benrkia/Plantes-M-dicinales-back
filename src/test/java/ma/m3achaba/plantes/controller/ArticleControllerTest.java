package ma.m3achaba.plantes.controller;

import ma.m3achaba.plantes.common.PageResponse;
import ma.m3achaba.plantes.dto.*;
import ma.m3achaba.plantes.exception.ResourceNotFoundException;
import ma.m3achaba.plantes.services.imp.ArticleService;
import ma.m3achaba.plantes.services.imp.CommentaireService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(controllers = ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @MockitoBean
    private ArticleService articleService;

    @MockitoBean
    private CommentaireService commentaireService;

    @InjectMocks
    private ArticleController articleController;

    @Autowired
    private MockMvc mockMvc;

    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
    }

    @Test
    void findById_ValidId_ReturnsArticle() throws Exception {
        ArticleResponse response = ArticleResponse.builder()
                .id(1L)
                .title("Test Article")
                .content("Test Content")
                .build();

        when(articleService.findById(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Article"));
    }

    @Test
    void findById_InvalidId_ReturnsNotFound() throws Exception {
        when(articleService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/articles/1"))
                .andExpect(status().isNotFound());
    }



    @Test
    void saveArticle_ValidRequest_ReturnsCreated() throws Exception {
        ArticleResponse response = ArticleResponse.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .build();

        when(articleService.save(any(ArticleRequest.class))).thenReturn(Optional.of(response));

        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("titre", "Test Title")
                        .param("content", "Test Content")
                        .param("Plante", "1,2"))  // Ensure the 'plante' field is included.
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"));
    }


    @Test
    void saveArticle_InvalidRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("content", "Test Content"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateArticle_ValidRequest_ReturnsOk() throws Exception {
        ArticleResponse response = ArticleResponse.builder()
                .id(1L)
                .title("Updated Title")
                .content("Updated Content")
                .build();

        when(articleService.update(any(ArticleRequest.class), any(Long.class))).thenReturn(Optional.of(response));

        mockMvc.perform(put("/api/articles/1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("titre", "Updated Title")
                        .param("content", "Updated Content")
                        .param("plante", "1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void updateArticle_InvalidRequest_ReturnsNotFound() throws Exception {
        when(articleService.update(any(ArticleRequest.class), any(Long.class)))
                .thenThrow(new ResourceNotFoundException("Article with ID 1 could not be updated. Please verify the provided data."));

        mockMvc.perform(put("/api/articles/1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("content", "Updated Content"))
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteArticle_ValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/articles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void saveCommentaire_ValidRequest_ReturnsCreated() throws Exception {
        CommentaireResponse response = new CommentaireResponse(1L, "Test Comment", "2024-12-29", "Test User");

        when(commentaireService.saveArticle(any(CommentaireRequest.class), any(Long.class))).thenReturn(Optional.of(response));

        mockMvc.perform(post("/api/articles/commentaire/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"commentaire\":\"Test Comment\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.commentaire").value("Test Comment"));
    }



    @Test
    void findAll_ValidRequestWithPaginationAndSearch_ReturnsArticles() throws Exception {
        // Mocked data
        PageResponse<ArticleResponse> response = PageResponse.<ArticleResponse>builder()
                .content(List.of(ArticleResponse.builder().id(1L).title("Test Article").content("Test Content").build()))
                .number(0)
                .size(10)
                .totalElements(1)
                .totalPages(1)
                .build();

        when(articleService.findAllWithSearch(0, 10, "Test")).thenReturn(response);

        mockMvc.perform(get("/api/articles")
                        .param("page", "0")
                        .param("size", "10")
                        .param("search", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("Test Article"));
    }

    @Test
    void findAll_ValidRequestWithPaginationAndPlanteFilter_ReturnsArticles() throws Exception {
        // Mocked data
        PageResponse<ArticleResponse> response = PageResponse.<ArticleResponse>builder()
                .content(List.of(ArticleResponse.builder().id(1L).title("Test Article").content("Test Content").build()))
                .number(0)
                .size(10)
                .totalElements(1)
                .totalPages(1)
                .build();

        when(articleService.findAllByMaladieName(0, 10, "Plante")).thenReturn(response);

        mockMvc.perform(get("/api/articles")
                        .param("page", "0")
                        .param("size", "10")
                        .param("plante", "Plante"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("Test Article"));
    }

    @Test
    void findAll_ValidRequestWithoutSearchAndPlante_ReturnsAllArticles() throws Exception {
        // Mocked data
        PageResponse<ArticleResponse> response = PageResponse.<ArticleResponse>builder()
                .content(List.of(ArticleResponse.builder().id(1L).title("Test Article").content("Test Content").build()))
                .number(0)
                .size(10)
                .totalElements(1)
                .totalPages(1)
                .build();

        when(articleService.findAll(0, 10)).thenReturn(response);

        mockMvc.perform(get("/api/articles")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("Test Article"));
    }


    // Test for the findAllArticles endpoint (returns all articles with optional search)
    @Test
    void findAllArticles_ValidRequestWithSearch_ReturnsArticles() throws Exception {
        // Mocked data
        List<ArticleResponse> responses = List.of( ArticleResponse.builder().id(1L).title("Test Article").content("Test Content").build());

        when(articleService.findAllWithSearch("Test")).thenReturn(responses);

        mockMvc.perform(get("/api/articles/list")
                        .param("search", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Article"));
    }

    @Test
    void findAllArticles_ValidRequestWithoutSearch_ReturnsAllArticles() throws Exception {
        // Mocked data
        List<ArticleResponse> responses = List.of( ArticleResponse.builder().id(1L).title("Test Article").content("Test Content").build());

        when(articleService.findAll()).thenReturn(responses);

        mockMvc.perform(get("/api/articles/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Article"));
    }

    // Test for the getCommentaire endpoint (fetch paginated comments for a given article)
    @Test
    void getCommentaire_ValidRequest_ReturnsComments() throws Exception {
        // Mocked data for comments
        PageResponse<CommentaireResponse> response = PageResponse.<CommentaireResponse>builder()
                .content(List.of(CommentaireResponse.builder().id(1L).name("Test User").commentaire("Test Comment").build()))
                .number(0)  // Corrected from 'page' to 'number'
                .size(5)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(commentaireService.listArticle(1L, 0, 5)).thenReturn(response);

        mockMvc.perform(get("/api/articles/commentaire/1")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].commentaire").value("Test Comment"));
    }

    @Test
    void getCommentaire_InvalidRequestForNonExistentArticle_ReturnsNotFound() throws Exception {
        // Assuming the service throws a ResourceNotFoundException for non-existing article
        when(commentaireService.listArticle(999L, 0, 5)).thenThrow(new ResourceNotFoundException("Article not found"));

        mockMvc.perform(get("/api/articles/commentaire/999")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isNotFound());
    }
}
