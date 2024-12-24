package ma.m3achaba.plantes.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import ma.m3achaba.plantes.validation.OnCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlantesRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidPlantesRequest() {
        PlantesRequest request = new PlantesRequest(
                "Menthe",
                "Une plante médicinale",
                "Infusion",
                "Maroc",
                "Ne pas consommer en excès",
                List.of(1, 2),
                new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[0])
        );

        Set<ConstraintViolation<PlantesRequest>> violations = validator.validate(request, OnCreate.class);

        assertEquals(0, violations.size(), "There should be no violations for a valid request.");
    }

    @Test
    void testInvalidPlantesRequest() {
        PlantesRequest request = new PlantesRequest(
                "", // Name is blank
                "", // Description is blank
                "", // Utilisation is blank
                "", // Region is blank
                "", // Precautions is blank
                null, // Maladies is null
                null // Images is null
        );

        Set<ConstraintViolation<PlantesRequest>> violations = validator.validate(request, OnCreate.class);

        assertEquals(6, violations.size(), "There should be 6 violations for an invalid request.");
    }

    @Test
    void testMissingName() {
        PlantesRequest request = new PlantesRequest(
                null, // Name is null
                "Une plante médicinale",
                "Infusion",
                "Maroc",
                "Ne pas consommer en excès",
                List.of(1, 2),
                new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[0])
        );

        Set<ConstraintViolation<PlantesRequest>> violations = validator.validate(request, OnCreate.class);

        assertEquals(1, violations.size(), "There should be 1 violation for missing name.");
        assertEquals("Nom must not be blank", violations.iterator().next().getMessage());
    }
}
