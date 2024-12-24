package ma.m3achaba.plantes.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentaireResponse {
    Long id;
    String commentaire;
    String date;
    String name;
}
