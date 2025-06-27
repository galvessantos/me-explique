package com.me_explique.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me_explique.service.SimplificadorService;


@RestController
@RequestMapping("/api/simplificar")
@Tag(name = "Simplificador de Texto", description = "API para simplificar textos para maior acessibilidade")
public class SimplificadorController {

    @Autowired
    private SimplificadorService service;

    @Operation(
            summary = "Simplifica um texto complexo",
            description = "Recebe um texto no corpo da requisição e o reescreve em linguagem clara e direta. Ideal para pessoas neurodivergentes.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Texto simplificado com sucesso.",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(type = "string"),
                                    examples = @ExampleObject(
                                            name = "Exemplo de Resposta",
                                            value = "Muitas coisas tornam este problema difícil."
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requisição inválida. O corpo da requisição está vazio.",
                            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno no servidor ao tentar simplificar o texto.",
                            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
                    )
            }
    )
    @PostMapping
    public String simplificar(@RequestBody String textoOCR) {
        try {
            return service.simplificarTexto(textoOCR);
        } catch (Exception e) {
            return "Erro ao simplificar texto: " + e.getMessage();
        }
    }
}
