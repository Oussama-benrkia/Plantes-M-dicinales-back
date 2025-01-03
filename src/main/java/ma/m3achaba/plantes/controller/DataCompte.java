package ma.m3achaba.plantes.controller;

import lombok.RequiredArgsConstructor;
import ma.m3achaba.plantes.dto.Data;
import ma.m3achaba.plantes.repo.ArticleRepository;
import ma.m3achaba.plantes.repo.MaladiesRepository;
import ma.m3achaba.plantes.repo.PlantesRepository;
import ma.m3achaba.plantes.repo.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/data")
public class DataCompte {
    private final MaladiesRepository maladiesRepository;
    private final PlantesRepository plantesRepository;
    private final ArticleRepository articleRepository;
    private final UserRepo userRepository;
    @GetMapping
    public ResponseEntity<Data> getdata(){

        Data data = Data.builder()
                .user(userRepository.count())
                .plante(plantesRepository.count())
                .Article(articleRepository.count())
                .maladie(maladiesRepository.count())
                .build();
        return ResponseEntity.ok(data);

    }
}
