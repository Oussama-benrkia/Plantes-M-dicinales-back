package ma.m3achaba.plantes.controller;
import lombok.RequiredArgsConstructor;
import ma.m3achaba.plantes.common.PageResponse;
import ma.m3achaba.plantes.dto.CommentaireRequest;
import ma.m3achaba.plantes.dto.CommentaireResponse;
import ma.m3achaba.plantes.dto.PlantesRequest;
import ma.m3achaba.plantes.dto.PlantesResponse;
import ma.m3achaba.plantes.exception.ResourceNotFoundException;
import ma.m3achaba.plantes.services.imp.CommentaireService;
import ma.m3achaba.plantes.services.imp.PlantesService;
import ma.m3achaba.plantes.validation.OnCreate;
import ma.m3achaba.plantes.validation.OnUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plantes")
public class PlantesController {
    private final PlantesService plantesService;
    private final CommentaireService commentaireService;
    @GetMapping("/{id}")
    public ResponseEntity<PlantesResponse> findById(@PathVariable Long id) {
        return plantesService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Plantes not found with id: " + id));
    }
    @GetMapping
    public ResponseEntity<PageResponse<PlantesResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String maladie
    ){
        PageResponse<PlantesResponse> response=null;
        if(maladie!=null && !maladie.isEmpty()){
            response=plantesService.findallbynameMaladie(page,size,maladie);
        }else {
            response = (search != null && !search.isEmpty())
                    ? plantesService.findAllWithsearch(page, size, search)
                    : plantesService.findAll(page, size);
        }


        return ResponseEntity.ok(response);
    }
    @GetMapping("/list")
    public ResponseEntity<List<PlantesResponse>> findAllPlantes(
            @RequestParam(required = false) String search
    ) {
        List<PlantesResponse> responses = (search != null && !search.isEmpty())
                ? plantesService.findAllWithSearch(search)
                : plantesService.findAll();

        return ResponseEntity.ok(responses);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PlantesResponse> savePlantes(
            @Validated(OnCreate.class) @ModelAttribute  PlantesRequest request
    ) {
        return plantesService.save(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .orElseThrow(() -> new ResourceNotFoundException("Failed to save Plantes. Please check your request."));
    }
    @PutMapping("/{id}")
    public ResponseEntity<PlantesResponse> updatePlantes(
            @PathVariable Long id,
            @Validated(OnUpdate.class) @ModelAttribute  PlantesRequest request
    ) {
        return plantesService.update(request, id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Plantes with ID " + id + " could not be updated. Please verify the provided data."));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlantes(@PathVariable Long id) {
        plantesService.delete(id);
    }


    @GetMapping("/{id}/associee")
    public ResponseEntity<PageResponse<PlantesResponse>> findById(@PathVariable Long id,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(plantesService.findplantassociee(id,page,size));
    }
    @PostMapping("/commentaire/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentaireResponse> saveCommentaire(@PathVariable Long id,
                                                               @Validated(OnCreate.class) @RequestBody CommentaireRequest request
    ) {
        return commentaireService.save_plante(request,id)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .orElseThrow(() -> new ResourceNotFoundException("Failed to save Plantes. Please check your request."));
    }
    @GetMapping("/commentaire/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PageResponse<CommentaireResponse>> GetComCommentaire(@PathVariable Long id,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(commentaireService.list_plnate(id,page,size));

    }
}
