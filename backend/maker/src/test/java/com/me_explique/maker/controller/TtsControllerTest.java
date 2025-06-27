package com.me_explique.maker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.me_explique.controller.TtsController;
import com.me_explique.service.TtsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TtsController.class)
class TtsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TtsService ttsServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TtsControllerTestConfig {

        @Bean
        @Primary
        public TtsService ttsService() {
            return Mockito.mock(TtsService.class);
        }
    }


    @Test
    void quandoConverterTextoParaAudio_deveRetornarArquivoMp3() throws Exception {
        String textoParaConverter = "Olá, mundo!";
        byte[] audioBytes = new byte[]{1, 2, 3};
        given(ttsServiceMock.convertTextToSpeech(anyString())).willReturn(audioBytes);

        mockMvc.perform(post("/api/tts/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(textoParaConverter))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "audio/mpeg"))
                .andExpect(content().bytes(audioBytes));
    }

}