package ma.m3achaba.plantes.controller;

import lombok.RequiredArgsConstructor;
import ma.m3achaba.plantes.common.PageResponse;
import ma.m3achaba.plantes.dto.*;
import ma.m3achaba.plantes.exception.ResourceNotFoundException;
import ma.m3achaba.plantes.services.imp.ArticleService;
import ma.m3achaba.plantes.services.imp.CommentaireService;
import ma.m3achaba.plantes.validation.OnCreate;
import ma.m3achaba.plantes.validation.OnUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {
    private final CommentaireService commentaireService;
    private final ArticleService articleService;

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> findById(@PathVariable Long id) {
        return articleService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with id: " + id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ArticleResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String plante

    ) {
        PageResponse<ArticleResponse> response=null;
        if(plante!=null && !plante.isEmpty()){
            response=articleService.findAllByMaladieName(page,size,plante);
        }else {
            response = (search != null && !search.isEmpty())
                    ? articleService.findAllWithSearch(page, size, search)
                    : articleService.findAll(page, size);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ArticleResponse>> findAllArticles(@RequestParam(required = false) String search) {
        List<ArticleResponse> responses = (search != null && !search.isEmpty())
                ? articleService.findAllWithSearch(search)
                : articleService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ArticleResponse> saveArticle(
            @Validated(OnCreate.class) @ModelAttribute ArticleRequest request
    ) {
        return articleService.save(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .orElseThrow(() -> new ResourceNotFoundException("Failed to save article. Please check your request."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable Long id,
            @Validated(OnUpdate.class) @ModelAttribute ArticleRequest request
    ) {
        return articleService.update(request, id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Article with ID " + id + " could not be updated. Please verify the provided data."));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable Long id) {
        articleService.delete(id);
    }
    @PostMapping("/commentaire/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentaireResponse> saveCommentaire(@PathVariable Long id,
                                                               @Validated(OnCreate.class) @RequestBody CommentaireRequest request
    ) {
        return commentaireService.saveArticle(request,id)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .orElseThrow(() -> new ResourceNotFoundException("Failed to save Plantes. Please check your request."));
    }
    @GetMapping("/commentaire/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PageResponse<CommentaireResponse>> getCommentaire(@PathVariable Long id,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(commentaireService.listArticle(id,page,size));

    }
}
