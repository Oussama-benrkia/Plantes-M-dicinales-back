package ma.m3achaba.plantes.services.imp;

import jakarta.persistence.EntityNotFoundException;
import ma.m3achaba.plantes.dto.PlantesRequest;
import ma.m3achaba.plantes.dto.PlantesResponse;
import ma.m3achaba.plantes.mapper.PlantesMapper;
import ma.m3achaba.plantes.model.Plantes;
import ma.m3achaba.plantes.repo.PlantesRepository;
import ma.m3achaba.plantes.repo.MaladiesRepository;
import ma.m3achaba.plantes.util.images.ImgService;
import ma.m3achaba.plantes.common.PageResponse;
import ma.m3achaba.plantes.model.Maladies;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlantesServiceTest {

    @Mock
    private PlantesRepository plantesRepository;

    @Mock
    private PlantesMapper plantesMapper;

    @Mock
    private MaladiesRepository maladiesRepository;

    @Mock
    private ImgService imgService;

    @InjectMocks
    private PlantesService plantesService;

    private Plantes plante;
    private PlantesRequest planteRequest;
    private PlantesResponse plantesResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the Plantes object before each test
        plante = new Plantes();
        plante.setId(1L);
        plante.setName("Rose");
        plante.setDescription("A beautiful flower");
        plante.setUtilisation("Decoration");
        plante.setRegion("Morocco");
        plante.setPrecautions("Handle with care");
        plante.setImages("path/to/image.jpg");
        plante.setMaladies(new ArrayList<>());  // Ensure that maladies list is initialized here

        // Initialize a PlantesRequest and response objects for testing
        planteRequest = new PlantesRequest("Rose", "A beautiful flower", "Decoration", "Morocco", "Handle with care", List.of(Math.toIntExact(1L)),null);
        plantesResponse = new PlantesResponse(1L, "Rose", "A beautiful flower", "Morocco", "Decoration", "Handle with care", "path/to/image.jpg", "2024-12-24", Arrays.asList("Malady 1"));
    }

    @Test
    void testFindAll() {
        Page<Plantes> page = mock(Page.class);
        when(page.getContent()).thenReturn(Collections.singletonList(plante));
        when(plantesRepository.findAll(any(Pageable.class))).thenReturn(page);

        PageResponse<PlantesResponse> response = plantesService.findAll(0, 10);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
    }

    @Test
    void testFindAllWithSearch() {
        Page<Plantes> page = mock(Page.class);
        when(page.getContent()).thenReturn(Collections.singletonList(plante));
        when(plantesRepository.findAllByNameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);

        PageResponse<PlantesResponse> response = plantesService.findAllWithsearch(0, 10, "Rose");

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
    }

    @Test
    void testFindAllByMaladie() {
        Maladies maladie = Maladies.builder()
                .id(1L)
                .name("Disease 1")
                .build();
        when(maladiesRepository.findByName("Disease 1")).thenReturn(Optional.of(maladie));
        when(plantesRepository.findAllByMaladies(any(Maladies.class), any(Pageable.class))).thenReturn(mock(Page.class));

        PageResponse<PlantesResponse> response = plantesService.findallbynameMaladie(0, 10, "Disease 1");

        assertNotNull(response);
    }

    @Test
    void testFindById() {
        // Given
        when(plantesRepository.findById(1L)).thenReturn(Optional.of(plante));
        when(plantesMapper.toResponse(plante)).thenReturn(plantesResponse);

        // When
        Optional<PlantesResponse> result = plantesService.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Rose", result.get().getName());
    }

    @Test
    void testSave() {
        // Given
        when(plantesRepository.existsByName("Rose")).thenReturn(false);
        when(maladiesRepository.findAllById(List.of(1L))).thenReturn(List.of(new Maladies()));
        when(plantesMapper.toEntity(planteRequest)).thenReturn(plante);
        when(plantesRepository.save(plante)).thenReturn(plante);
        when(plantesMapper.toResponse(plante)).thenReturn(plantesResponse);

        // When
        Optional<PlantesResponse> result = plantesService.save(planteRequest);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Rose", result.get().getName());
    }

    @Test
    void testSaveEntityAlreadyExists() {
        // Given
        when(plantesRepository.existsByName("Rose")).thenReturn(true);

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> plantesService.save(planteRequest));
    }




    @Test
    void testFindAllWithSearch2() {
        // Given
        List<Plantes> plantesList = List.of(plante);
        Page<Plantes> page = Mockito.mock(Page.class);
        when(plantesRepository.findAllByNameContainingIgnoreCase("Rose", PageRequest.of(0, 10))).thenReturn(page);
        when(page.getContent()).thenReturn(plantesList);
        when(plantesMapper.toResponse(plante)).thenReturn(plantesResponse);

        // When
        PageResponse<PlantesResponse> result = plantesService.findAllWithsearch(0, 10, "Rose");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindAllByMaladieName() {
        // Given
        Maladies maladie = new Maladies();
        maladie.setName("Disease");
        when(maladiesRepository.findByName("Disease")).thenReturn(Optional.of(maladie));
        when(plantesRepository.findAllByMaladies(maladie, PageRequest.of(0, 10))).thenReturn(Mockito.mock(Page.class));

        // When
        PageResponse<PlantesResponse> result = plantesService.findallbynameMaladie(0, 10, "Disease");

        // Then
        assertNotNull(result);
    }
}
