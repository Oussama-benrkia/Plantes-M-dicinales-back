package ma.m3achaba.plantes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.m3achaba.plantes.common.PageResponse;
import ma.m3achaba.plantes.dto.PlantesRequest;
import ma.m3achaba.plantes.dto.PlantesResponse;
import ma.m3achaba.plantes.services.imp.PlantesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PlantesController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
class PlantesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean // Change @Mock to @MockBean
    private PlantesService plantesService;

    private PlantesResponse mockResponse;
    private PlantesRequest mockRequest;

    @BeforeEach
    void setUp() {
        // Your existing setUp code remains the same
        mockResponse = PlantesResponse.builder()
                .id(1L)
                .name("Test Plante")
                .description("Test Description")
                .region("Test Region")
                .utilisation("Test Utilisation")
                .precautions("Test Precautions")
                .maladies(Arrays.asList("Maladie1", "Maladie2"))
                .build();

        mockRequest = new PlantesRequest(
                "Test Plante",
                "Test Description",
                "Test Utilisation",
                "Test Region",
                "Test Precautions",
                Arrays.asList(1, 2),
                null
        );

    }

    @Test
    void findById_ExistingId_ReturnsOk() throws Exception {
        when(plantesService.findById(1L)).thenReturn(Optional.of(mockResponse));

        mockMvc.perform(get("/api/plantes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Plante"));
    }

    @Test
    void findById_NonExistingId_ThrowsResourceNotFoundException() throws Exception {
        when(plantesService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/plantes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_WithoutSearch_ReturnsPageResponse() throws Exception {
        // Mock data
        PageResponse<PlantesResponse> pageResponse = PageResponse.<PlantesResponse>builder()
                .content(Arrays.asList(mockResponse))
                .build();

        when(plantesService.findAll(0, 10)).thenReturn(pageResponse);

        // Perform the GET request without search or maladie
        mockMvc.perform(get("/api/plantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Plante"));
    }

    @Test
    void findAll_WithSearch_ReturnsFilteredPageResponse() throws Exception {
        // Mock data with search filter
        PageResponse<PlantesResponse> pageResponse = PageResponse.<PlantesResponse>builder()
                .content(Arrays.asList(mockResponse))
                .build();

        when(plantesService.findAllWithsearch(0, 10, "Test")).thenReturn(pageResponse);

        // Perform the GET request with search parameter
        mockMvc.perform(get("/api/plantes")
                        .param("search", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Plante"));
    }

    @Test
    void findAll_WithMaladie_ReturnsFilteredPageResponse() throws Exception {
        // Mock data with maladie filter
        PageResponse<PlantesResponse> pageResponse = PageResponse.<PlantesResponse>builder()
                .content(Arrays.asList(mockResponse))
                .build();

        when(plantesService.findallbynameMaladie(0, 10, "1")).thenReturn(pageResponse);

        // Perform the GET request with maladie parameter
        mockMvc.perform(get("/api/plantes")
                        .param("maladie", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Plante"));
    }

    @Test
    void findAll_WithSearchAndMaladie_ReturnsFilteredPageResponse() throws Exception {
        PageResponse<PlantesResponse> pageResponse = PageResponse.<PlantesResponse>builder()
                .content(Arrays.asList(mockResponse))
                .build();

        when(plantesService.findAllWithsearch(0, 10, "Test")).thenReturn(pageResponse);
        when(plantesService.findallbynameMaladie(0, 10, "1")).thenReturn(pageResponse);

        mockMvc.perform(get("/api/plantes")
                        .param("maladie", "1"))
                .andExpect(status().isOk()) // Expecting 200 OK since there's content
                .andExpect(jsonPath("$.content[0].name").value("Test Plante"));
    }



    @Test
    void updatePlantes_ValidRequest_UpdatesResource() throws Exception {
        when(plantesService.update(any(PlantesRequest.class), eq(1L)))
                .thenReturn(Optional.of(mockResponse));

        mockMvc.perform(put("/api/plantes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Plante"));
    }

    @Test
    void deletePlantes_ExistingId_DeletesResource() throws Exception {
        when(plantesService.delete(1L)).thenReturn(Optional.of(mockResponse));

        mockMvc.perform(delete("/api/plantes/1"))
                .andExpect(status().isNoContent()); // Expecting HTTP 204 No Content status
    }
    @Test
    void findById_ReturnsPlantesAssociee() throws Exception {
        // Create mock data for the test
        PlantesResponse mockResponse = new PlantesResponse();
        mockResponse.setName("Test Plante");

        PageResponse<PlantesResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(Collections.singletonList(mockResponse)); // You can add more elements if needed
        pageResponse.setTotalElements(1);
        pageResponse.setTotalPages(1);

        // Mock the service call
        when(plantesService.findplantassociee(1L, 0, 5)).thenReturn(pageResponse);

        // Perform the GET request and check the response
        mockMvc.perform(get("/api/plantes/1/associee")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Plante"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        // Verify that the service method was called with the correct parameters
        verify(plantesService, times(1)).findplantassociee(1L, 0, 5);
    }
    @Test
    void findAllPlantes_WithSearch_ReturnsFilteredList() throws Exception {
        // Mock data for the test
        PlantesResponse mockPlante1 = new PlantesResponse();
        mockPlante1.setName("Test Plante 1");

        PlantesResponse mockPlante2 = new PlantesResponse();
        mockPlante2.setName("Test Plante 2");

        List<PlantesResponse> mockList = Arrays.asList(mockPlante1, mockPlante2);

        // Mock the service method when search is applied
        when(plantesService.findAllWithSearch("Test")).thenReturn(mockList);

        // Perform the GET request with search parameter
        mockMvc.perform(get("/api/plantes/list")
                        .param("search", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))  // Expecting two items in the response
                .andExpect(jsonPath("$[0].name").value("Test Plante 1"))
                .andExpect(jsonPath("$[1].name").value("Test Plante 2"));

        // Verify that the service method was called with the search parameter
        verify(plantesService, times(1)).findAllWithSearch("Test");
    }

    @Test
    void findAllPlantes_WithoutSearch_ReturnsAllList() throws Exception {
        // Mock data for the test
        PlantesResponse mockPlante1 = new PlantesResponse();
        mockPlante1.setName("Test Plante 1");

        PlantesResponse mockPlante2 = new PlantesResponse();
        mockPlante2.setName("Test Plante 2");

        List<PlantesResponse> mockList = Arrays.asList(mockPlante1, mockPlante2);

        // Mock the service method when no search is applied
        when(plantesService.findAll()).thenReturn(mockList);

        // Perform the GET request without search parameter
        mockMvc.perform(get("/api/plantes/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))  // Expecting two items in the response
                .andExpect(jsonPath("$[0].name").value("Test Plante 1"))
                .andExpect(jsonPath("$[1].name").value("Test Plante 2"));

        // Verify that the service method was called with no search parameter
        verify(plantesService, times(1)).findAll();
    }
}
