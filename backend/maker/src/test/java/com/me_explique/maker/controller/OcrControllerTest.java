package com.me_explique.maker.controller;

import com.drew.imaging.ImageMetadataReader;
import com.me_explique.controller.OcrController;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OcrControllerTest {

    private MockMvc mockMvc;
    private MockedStatic<ImageIO> imageIOStaticMock;
    private MockedStatic<ImageMetadataReader> metadataReaderStaticMock;
    private MockedConstruction<Tesseract> tesseractConstructionMock;

    @BeforeEach
    public void setUp() {
        imageIOStaticMock = mockStatic(ImageIO.class);
        metadataReaderStaticMock = mockStatic(ImageMetadataReader.class);

        tesseractConstructionMock = mockConstruction(Tesseract.class, (mock, context) -> {
            when(mock.doOCR(any(BufferedImage.class))).thenReturn("Texto extraído com sucesso");
        });

        mockMvc = MockMvcBuilders.standaloneSetup(new OcrController()).build();
    }

    @Test
    public void quandoEnviarImagemValida_deveRetornarTextoExtraido() throws Exception {
        MockMultipartFile imagemValida = new MockMultipartFile("imagem", "teste.png", "image/png", "bytes".getBytes());

        BufferedImage realBlankImage = new BufferedImage(100, 50, BufferedImage.TYPE_BYTE_GRAY);

        imageIOStaticMock.when(() -> ImageIO.read(any(InputStream.class))).thenReturn(realBlankImage);

        metadataReaderStaticMock.when(() -> ImageMetadataReader.readMetadata(any(InputStream.class)))
                .thenReturn(mock(com.drew.metadata.Metadata.class));

        mockMvc.perform(multipart("/api/ocr/ler-imagem").file(imagemValida))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.TextoOCR").value("Texto extraído com sucesso"));
    }

    @Test
    public void quandoImagemForInvalida_deveRetornarBadRequest() throws Exception {
        MockMultipartFile arquivoInvalido = new MockMultipartFile("imagem", "invalido.png", "image/png", "bytes invalidos".getBytes());
        imageIOStaticMock.when(() -> ImageIO.read(any(InputStream.class))).thenReturn(null);

        mockMvc.perform(multipart("/api/ocr/ler-imagem").file(arquivoInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Imagem inválida ou formato não suportado."));
    }


    @AfterEach
    public void tearDown() {
        imageIOStaticMock.close();
        metadataReaderStaticMock.close();
        tesseractConstructionMock.close();
    }
}