package ma.m3achaba.plantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    Long plante;
    Long Article;
    Long user;
    Long maladie;
}
