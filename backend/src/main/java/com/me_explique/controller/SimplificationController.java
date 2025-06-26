package main.java.com.me_explique.controller;

import com.me_explique.dto.ResponseDTO;
import com.me_explique.dto.SimplifyRequestDTO;
import com.me_explique.service.SimplificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simplify")
public class SimplificationController {

    @Autowired
    private SimplificationService simplificationService;

    @PostMapping
    public ResponseEntity<?> simplificarTexto(@RequestBody SimplifyRequestDTO request) {
        String simplificado = simplificationService.simplificarTexto(request.getText());
        return ResponseEntity.ok().body(
            new ResponseDTO("Texto simplificado com sucesso.", simplificado)
        );
    }
}
