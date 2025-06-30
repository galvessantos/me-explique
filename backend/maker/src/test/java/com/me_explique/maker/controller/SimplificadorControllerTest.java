package com.me_explique.maker.controller;

import com.me_explique.controller.SimplificadorController;
import com.me_explique.service.SimplificadorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SimplificadorController.class)
public class SimplificadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimplificadorService simplificadorService;

    @Test
    @DisplayName("Deve simplificar o texto com sucesso e retornar status 200")
    void quandoSimplificar_comTextoValido_deveRetornarTextoSimplificado() throws Exception {
        String textoOriginal = "Existem múltiplas variáveis que corroboram para a complexidade intrínseca do problema em questão.";
        String textoSimplificadoEsperado = "Muitas coisas tornam este problema difícil.";

        given(simplificadorService.simplificarTexto(anyString())).willReturn(textoSimplificadoEsperado);

        mockMvc.perform(post("/api/simplificar")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(textoOriginal))
                .andExpect(status().isOk())
                .andExpect(content().string(textoSimplificadoEsperado));
    }

    @Test
    @DisplayName("Deve retornar mensagem de erro quando o serviço falhar")
    void quandoServicoLancaExcecao_deveRetornarMensagemDeErro() throws Exception {
        String textoOriginal = "Qualquer texto.";
        String mensagemDeErro = "Falha na comunicação com a API externa.";

        given(simplificadorService.simplificarTexto(anyString())).willThrow(new RuntimeException(mensagemDeErro));

        mockMvc.perform(post("/api/simplificar")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(textoOriginal))
                .andExpect(status().isOk())
                .andExpect(content().string("Erro ao simplificar texto: " + mensagemDeErro));
    }
}