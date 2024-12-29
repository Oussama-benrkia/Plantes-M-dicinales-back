package ma.m3achaba.plantes.mapper;

import lombok.RequiredArgsConstructor;
import ma.m3achaba.plantes.dto.ArticleRequest;
import ma.m3achaba.plantes.dto.ArticleResponse;
import ma.m3achaba.plantes.model.Article;
import ma.m3achaba.plantes.model.Plantes;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ArticleMapper implements Mapper<Article, ArticleResponse, ArticleRequest> {
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final Environment environment;

    @Override
    public Article toEntity(ArticleRequest request) {
        return Article.builder()
                .title(request.titre())
                .content(request.content())
                .build();
    }

    @Override
    public ArticleResponse toResponse(Article article) {
        String serverAddress = environment.getProperty("server.address", "localhost");
        String serverPort = environment.getProperty("server.port", "8080");

        String imageUrl = "";
        if (article.getImage() != null && !article.getImage().isEmpty()) {
            imageUrl = "http://" + serverAddress + ":" + serverPort + "/api/image/" + article.getImage();
        }
        ArticleResponse response = new ArticleResponse();
        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setContent(article.getContent());
        response.setImage(imageUrl);
        response.setPlante(article.getPlantes().stream().map(Plantes::getId).toList());
        response.setDate(article.getCreatedDate().format(formatter)) ;// Assurez-vous que `getCreatedDate` est défini
        return response;
    }
}
