package ma.m3achaba.plantes.mapper;

import lombok.RequiredArgsConstructor;
import ma.m3achaba.plantes.dto.PlantesRequest;
import ma.m3achaba.plantes.dto.PlantesResponse;
import ma.m3achaba.plantes.model.Maladies;
import ma.m3achaba.plantes.model.Plantes;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class PlantesMapper implements Mapper<Plantes, PlantesResponse, PlantesRequest>{
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final Environment environment;

    @Override
    public Plantes toEntity(final PlantesRequest plantesRequest) {
        if (plantesRequest == null) {
            return null;
        }
        return Plantes.builder()
                .name(plantesRequest.name())
                .description(plantesRequest.description())
                .precautions(plantesRequest.precautions())
                .region(plantesRequest.region())
                .utilisation(plantesRequest.utilisation())
                .build();
    }

    @Override
    public PlantesResponse toResponse(final Plantes plantes) {
        if (plantes == null) {
            return null;
        }
        String serverAddress = environment.getProperty("server.address", "localhost");
        String serverPort = environment.getProperty("server.port", "8080");

        String imageUrl = "";
        if (plantes.getImages() != null && !plantes.getImages().isEmpty()) {
            imageUrl = "http://" + serverAddress + ":" + serverPort + "/api/image/" + plantes.getImages();
        }

        return PlantesResponse.builder()
                .id(plantes.getId())
                .description(plantes.getDescription())
                .precautions(plantes.getPrecautions())
                .region(plantes.getRegion())
                .utilisation(plantes.getUtilisation())
                .name(plantes.getName())
                .image(imageUrl)
                .maladies(plantes.getMaladies().stream().map(Maladies::getName).toList())
                .dateCreated(plantes.getCreatedDate().format(formatter))
                .build();
    }

}
