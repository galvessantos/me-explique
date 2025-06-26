package main.java.com.me_explique.controller;

import com.me_explique.dto.OcrRequestDTO;
import com.me_explique.dto.ResponseDTO;
import com.me_explique.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ocr")
public class OcrController {

    @Autowired
    private OcrService ocrService;

    @PostMapping
    public ResponseEntity<?> extrairTexto(@RequestBody OcrRequestDTO request) {
        String texto = ocrService.extrairTextoDaImagem(request.getImageId());
        return ResponseEntity.ok().body(
            new ResponseDTO("Texto extraído com sucesso.", texto)
        );
    }
}
