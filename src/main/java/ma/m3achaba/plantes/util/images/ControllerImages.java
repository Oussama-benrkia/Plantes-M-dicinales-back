package ma.m3achaba.plantes.util.images;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ControllerImages {
    private final ImgService imgService;
    @GetMapping("/{folder}/{file}")
    public ResponseEntity<byte[]> image(@PathVariable String folder, @PathVariable String file) throws IOException {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Set appropriate content type
                .body(imgService.imageToByte(folder+"/"+file));
    }
}
