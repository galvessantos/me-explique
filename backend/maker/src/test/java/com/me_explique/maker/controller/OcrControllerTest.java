package com.me_explique.maker.controller;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.me_explique.controller.OcrController;
import com.me_explique.service.SimplificadorService;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class OcrControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SimplificadorService mockSimplificadorService;

    @InjectMocks
    private OcrController ocrController;

    private MockedStatic<ImageIO> imageIOStaticMock;
    private MockedStatic<ImageMetadataReader> metadataReaderStaticMock;

    @BeforeEach
    public void setUp() {
        imageIOStaticMock = mockStatic(ImageIO.class);
        metadataReaderStaticMock = mockStatic(ImageMetadataReader.class);
        mockMvc = MockMvcBuilders.standaloneSetup(ocrController).build();

        java.net.URL resource = getClass().getClassLoader().getResource("tessdata");
        assertNotNull(resource, "O diretório 'src/test/resources/tessdata' deve existir para que os testes passem.");
    }

    @AfterEach
    public void tearDown() {
        imageIOStaticMock.close();
        metadataReaderStaticMock.close();
    }

    @Test
    public void quandoEnviarImagemValida_deveRetornarTextosOriginalESimplificado() throws Exception {
        MockMultipartFile imagemValida = new MockMultipartFile("file", "teste.png", "image/png", "bytes".getBytes());
        BufferedImage realBlankImage = new BufferedImage(100, 50, BufferedImage.TYPE_BYTE_GRAY);

        imageIOStaticMock.when(() -> ImageIO.read(any(InputStream.class))).thenReturn(realBlankImage);
        metadataReaderStaticMock.when(() -> ImageMetadataReader.readMetadata(any(InputStream.class))).thenReturn(mock(Metadata.class));

        String textoDoOcr = "   texto extraído com \n\r problemas de formatação. ";
        String textoLimpoEsperado = "texto extraído com \n problemas de formatação.";
        String textoSimplificadoEsperado = "Texto simplificado pelo serviço mockado.";

        when(mockSimplificadorService.simplificarTexto(textoLimpoEsperado)).thenReturn(textoSimplificadoEsperado);

        try (MockedConstruction<Tesseract> tesseractConstructionMock = mockConstruction(Tesseract.class, (mock, context) -> {
            when(mock.doOCR(any(BufferedImage.class))).thenReturn(textoDoOcr);
            doNothing().when(mock).setDatapath(anyString());
        })) {
            mockMvc.perform(multipart("/api/ocr/ler-imagem").file(imagemValida))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.textoOriginal").value(textoLimpoEsperado))
                    .andExpect(jsonPath("$.textoSimplificado").value(textoSimplificadoEsperado));
        }

        verify(mockSimplificadorService, times(1)).simplificarTexto(textoLimpoEsperado);
    }

    @Test
    public void quandoOcrNaoDetectaTexto_deveRetornarMensagemApropriada() throws Exception {
        MockMultipartFile imagemVazia = new MockMultipartFile("file", "vazia.png", "image/png", "bytes".getBytes());
        BufferedImage realBlankImage = new BufferedImage(100, 50, BufferedImage.TYPE_BYTE_GRAY);

        imageIOStaticMock.when(() -> ImageIO.read(any(InputStream.class))).thenReturn(realBlankImage);
        metadataReaderStaticMock.when(() -> ImageMetadataReader.readMetadata(any(InputStream.class))).thenReturn(mock(Metadata.class));

        String textoDoOcr = "   ";
        String textoLimpoEsperado = "Nenhum texto detectado";
        String textoSimplificadoEsperado = "Texto simplificado da mensagem de erro.";

        when(mockSimplificadorService.simplificarTexto(textoLimpoEsperado)).thenReturn(textoSimplificadoEsperado);

        try (MockedConstruction<Tesseract> tesseractConstructionMock = mockConstruction(Tesseract.class, (mock, context) -> {
            when(mock.doOCR(any(BufferedImage.class))).thenReturn(textoDoOcr);
            doNothing().when(mock).setDatapath(anyString());
        })) {
            mockMvc.perform(multipart("/api/ocr/ler-imagem").file(imagemVazia))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.textoOriginal").value(textoLimpoEsperado))
                    .andExpect(jsonPath("$.textoSimplificado").value(textoSimplificadoEsperado));
        }

        verify(mockSimplificadorService, times(1)).simplificarTexto(textoLimpoEsperado);
    }


    @Test
    public void quandoImagemForInvalida_deveRetornarBadRequest() throws Exception {
        MockMultipartFile arquivoInvalido = new MockMultipartFile("file", "invalido.png", "image/png", "bytes".getBytes());
        imageIOStaticMock.when(() -> ImageIO.read(any(InputStream.class))).thenReturn(null);

        mockMvc.perform(multipart("/api/ocr/ler-imagem").file(arquivoInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Imagem inválida ou formato não suportado."));

        verifyNoInteractions(mockSimplificadorService);
    }

    @Test
    public void quandoOcrLancaExcecao_deveRetornarErro500() throws Exception {
        MockMultipartFile imagemValida = new MockMultipartFile("file", "teste.png", "image/png", "bytes".getBytes());
        BufferedImage realBlankImage = new BufferedImage(100, 50, BufferedImage.TYPE_BYTE_GRAY);

        imageIOStaticMock.when(() -> ImageIO.read(any(InputStream.class))).thenReturn(realBlankImage);
        metadataReaderStaticMock.when(() -> ImageMetadataReader.readMetadata(any(InputStream.class))).thenReturn(mock(Metadata.class));

        String mensagemErro = "Falha no motor Tesseract";

        try (MockedConstruction<Tesseract> tesseractConstructionMock = mockConstruction(Tesseract.class, (mock, context) -> {
            when(mock.doOCR(any(BufferedImage.class))).thenThrow(new TesseractException(mensagemErro));
            doNothing().when(mock).setDatapath(anyString());
        })) {
            mockMvc.perform(multipart("/api/ocr/ler-imagem").file(imagemValida))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.erro").value("Erro ao processar OCR: " + mensagemErro));
        }

        verifyNoInteractions(mockSimplificadorService);
    }

    @Test
    public void quandoSimplificadorServiceLancaExcecao_deveRetornarErro500() throws Exception {
        MockMultipartFile imagemValida = new MockMultipartFile("file", "teste.png", "image/png", "bytes".getBytes());
        BufferedImage realBlankImage = new BufferedImage(100, 50, BufferedImage.TYPE_BYTE_GRAY);

        imageIOStaticMock.when(() -> ImageIO.read(any(InputStream.class))).thenReturn(realBlankImage);
        metadataReaderStaticMock.when(() -> ImageMetadataReader.readMetadata(any(InputStream.class))).thenReturn(mock(Metadata.class));

        String textoDoOcr = "texto qualquer";
        String textoLimpoEsperado = "texto qualquer";
        String mensagemErro = "Erro de rede na API";

        when(mockSimplificadorService.simplificarTexto(textoLimpoEsperado)).thenThrow(new IOException(mensagemErro));

        try (MockedConstruction<Tesseract> tesseractConstructionMock = mockConstruction(Tesseract.class, (mock, context) -> {
            when(mock.doOCR(any(BufferedImage.class))).thenReturn(textoDoOcr);
            doNothing().when(mock).setDatapath(anyString());
        })) {
            mockMvc.perform(multipart("/api/ocr/ler-imagem").file(imagemValida))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.erro").value("Erro ao ler imagem: " + mensagemErro));
        }

        verify(mockSimplificadorService, times(1)).simplificarTexto(textoLimpoEsperado);
    }
}