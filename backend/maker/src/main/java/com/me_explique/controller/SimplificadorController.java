package com.me_explique.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.me_explique.service.SimplificadorService;


@RestController
@RequestMapping("/api/simplificar")
public class SimplificadorController {

    @Autowired
    private SimplificadorService service;

    @PostMapping
    public String simplificar(@RequestBody String textoOCR) {
        try {
            return service.simplificarTexto(textoOCR);
        } catch (Exception e) {
            return "Erro ao simplificar texto: " + e.getMessage();
        }
    }
}
