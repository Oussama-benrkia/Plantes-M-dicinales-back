package ma.m3achaba.plantes.mapper;

import ma.m3achaba.plantes.dto.PlantesRequest;
import ma.m3achaba.plantes.dto.PlantesResponse;
import ma.m3achaba.plantes.model.Maladies;
import ma.m3achaba.plantes.model.Plantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlantesMapperTest {

    @Mock
    private Environment environment;

    @InjectMocks
    private PlantesMapper plantesMapper;

    private PlantesRequest plantesRequest;
    private Plantes plantes;
    private List<Maladies> maladies;

    @BeforeEach
    void setUp() {
        // Configure les données de test
        plantesRequest = new PlantesRequest(
                "Menthe",
                "Une plante médicinale",
                "Infusion",
                "Maroc",
                "Ne pas consommer en excès",
                List.of(1, 2),
                null // Pas d'image dans la requête
        );

        maladies = List.of(
                Maladies.builder()
                        .name("maladies_test")
                        .id(1L)
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build(),
                Maladies.builder()
                        .name("maladies_test")
                        .id(1L)
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build()
        );

        plantes = Plantes.builder()
                .id(1L)
                .name("Menthe")
                .description("Une plante médicinale")
                .utilisation("Infusion")
                .region("Maroc")
                .precautions("Ne pas consommer en excès")
                .createdDate(LocalDateTime.of(2024, 12, 24, 10, 0))
                .maladies(maladies)
                .images("menthe.jpg")
                .build();

        lenient().when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");
        lenient().when(environment.getProperty("server.port", "8080")).thenReturn("8080");
    }

    @Test
    void testToEntity() {
        Plantes entity = plantesMapper.toEntity(plantesRequest);

        assertNotNull(entity);
        assertEquals(plantesRequest.name(), entity.getName());
        assertEquals(plantesRequest.description(), entity.getDescription());
        assertEquals(plantesRequest.utilisation(), entity.getUtilisation());
        assertEquals(plantesRequest.region(), entity.getRegion());
        assertEquals(plantesRequest.precautions(), entity.getPrecautions());
    }

    @Test
    void testToEntityWithNullRequest() {
        Plantes entity = plantesMapper.toEntity(null);

        assertNull(entity);
    }

    @Test
    void testToResponse() {
        PlantesResponse response = plantesMapper.toResponse(plantes);

        assertNotNull(response);
        assertEquals(plantes.getId(), response.getId());
        assertEquals(plantes.getName(), response.getName());
        assertEquals(plantes.getDescription(), response.getDescription());
        assertEquals(plantes.getUtilisation(), response.getUtilisation());
        assertEquals(plantes.getRegion(), response.getRegion());
        assertEquals(plantes.getPrecautions(), response.getPrecautions());
        assertEquals(
                maladies.stream().map(Maladies::getName).collect(Collectors.toList()),
                response.getMaladies()
        );
        assertEquals("http://localhost:8080/api/image/menthe.jpg", response.getImage());
        assertEquals("24/12/2024", response.getDateCreated());
    }

    @Test
    void testToResponseWithNullEntity() {
        PlantesResponse response = plantesMapper.toResponse(null);

        assertNull(response);
    }

    @Test
    void testToResponseWithoutImage() {
        plantes.setImages(null);

        PlantesResponse response = plantesMapper.toResponse(plantes);

        assertNotNull(response);
        assertEquals("", response.getImage());
    }
}
