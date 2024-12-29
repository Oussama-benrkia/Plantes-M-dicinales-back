package ma.m3achaba.plantes.mapper;

import ma.m3achaba.plantes.dto.CommentaireRequest;
import ma.m3achaba.plantes.dto.CommentaireResponse;
import ma.m3achaba.plantes.model.Commentaire_article;
import ma.m3achaba.plantes.model.Commentaire_plant;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
@Component
public class CommentaireMapper {
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public Commentaire_plant toEntityPlante(final CommentaireRequest cmt) {
        return Commentaire_plant.builder()
                .message(cmt.commentaire())
                .build();
    }
    public CommentaireResponse toResponse(final Commentaire_plant cmt) {

        return CommentaireResponse.builder()
                .commentaire(cmt.getMessage())
                .date(formatter.format(cmt.getCreatedDate()))
                .name(cmt.getUtilisateur().getNom())
                .id(cmt.getId())
                .build();

    }
    public Commentaire_article toEntityArticle(final CommentaireRequest cmt) {
        return Commentaire_article.builder()
                .message(cmt.commentaire())
                .build();
    }
    public CommentaireResponse toResponse(final Commentaire_article cmt) {

        return CommentaireResponse.builder()
                .commentaire(cmt.getMessage())
                .date(formatter.format(cmt.getCreatedDate()))
                .name(cmt.getUtilisateur().getNom())
                .id(cmt.getId())
                .build();

    }
}
