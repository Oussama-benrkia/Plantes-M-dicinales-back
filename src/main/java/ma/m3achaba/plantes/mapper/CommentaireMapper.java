package ma.m3achaba.plantes.mapper;

import ma.m3achaba.plantes.dto.CommentaireRequest;
import ma.m3achaba.plantes.dto.CommentaireResponse;
import ma.m3achaba.plantes.model.ArticleComment;
import ma.m3achaba.plantes.model.PlantComment;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
@Component
public class CommentaireMapper {
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public PlantComment toEntityPlante(final CommentaireRequest cmt) {
        return PlantComment.builder()
                .message(cmt.commentaire())
                .build();
    }
    public CommentaireResponse toResponse(final PlantComment cmt) {

        return CommentaireResponse.builder()
                .commentaire(cmt.getMessage())
                .date(formatter.format(cmt.getCreatedDate()))
                .name(cmt.getUtilisateur().getNom())
                .id(cmt.getId())
                .build();

    }
    public ArticleComment toEntityArticle(final CommentaireRequest cmt) {
        return ArticleComment.builder()
                .message(cmt.commentaire())
                .build();
    }
    public CommentaireResponse toResponse(final ArticleComment cmt) {

        return CommentaireResponse.builder()
                .commentaire(cmt.getMessage())
                .date(formatter.format(cmt.getCreatedDate()))
                .name(cmt.getUtilisateur().getNom())
                .id(cmt.getId())
                .build();

    }
}
