package main.java.com.me_explique.controller;

import main.java.com.me_explique.dto.ResponseDTO;
import main.java.com.me_explique.service.StatusService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @GetMapping
    public Map<String, Object> getStatus() {
        return Map.of(
            "status", "ok",
            "components", Map.of(
                "ocr", "ativo",
                "gpt", "ativo",
                "tts", "ativo"
            )
        );
    }
}
