package ma.m3achaba.plantes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ma.m3achaba.plantes.validation.OnCreate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ArticleRequest(

        @NotBlank(groups = OnCreate.class, message = "titre must not be blank")
        String titre,

        @NotBlank(groups = OnCreate.class, message = "content must not be blank")
        String content,
        @NotNull(groups = OnCreate.class, message = "plante must not be blank")
        List<Integer> Plante,

        MultipartFile images
) {
}
