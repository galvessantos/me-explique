package main.java.com.me_explique.controller;

import com.me_explique.service.UploadService;
import com.me_explique.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping
    public ResponseEntity<?> uploadImagem(@RequestParam("image") MultipartFile image) {
        UUID imageId = uploadService.salvarImagem(image);
        return ResponseEntity.ok().body(
            new ResponseDTO("Imagem recebida com sucesso.", imageId.toString())
        );
    }
}