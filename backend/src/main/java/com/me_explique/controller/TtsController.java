package main.java.com.me_explique.controller;

import com.me_explique.dto.ResponseDTO;
import com.me_explique.dto.TtsRequestDTO;
import com.me_explique.service.TtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tts")
public class TtsController {

    @Autowired
    private TtsService ttsService;

    @PostMapping
    public ResponseEntity<?> gerarAudio(@RequestBody TtsRequestDTO request) {
        String audioBase64 = ttsService.converterTextoEmAudio(request.getText());
        return ResponseEntity.ok().body(
            new ResponseDTO("Áudio gerado com sucesso.", audioBase64)
        );
    }
}
