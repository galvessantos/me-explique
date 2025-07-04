package com.me_explique.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sourceforge.tess4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.me_explique.service.SimplificadorService;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.drew.imaging.ImageMetadataReader; // Para ler metadados EXIF
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

@RestController
@RequestMapping("/api/ocr")
@Tag(name = "OCR", description = "Serviço para Reconhecimento Óptico de Caracteres")
public class OcrController {

    @Autowired
    private SimplificadorService service;

    @Operation(
            summary = "Extrai e simplifica texto de uma imagem",
            description = "Recebe um arquivo de imagem (PNG, JPG, etc.), processa-a para melhorar a qualidade, extrai o texto com Tesseract e retorna uma versão original e outra simplificada.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Textos extraído e simplificado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"textoOriginal\": \"Texto extraído da imagem\",\"textoSimplificado\": \"Versão simplificada do texto.\"}"))),
                    @ApiResponse(responseCode = "400", description = "Imagem inválida ou formato não suportado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"erro\": \"Imagem inválida ou formato não suportado.\"}"))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor durante o processamento", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"erro\": \"Erro ao processar OCR: ...\"}")))
            }
    )
    @PostMapping("/ler-imagem")
    public ResponseEntity<?> lerImagem(@RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            BufferedImage originalImage = ImageIO.read(inputStream);

            if (originalImage == null) {
                return ResponseEntity.status(400).body("{\"erro\": \"Imagem inválida ou formato não suportado.\"}");
            }

            originalImage = corrigirOrientacao(originalImage, file);

            RescaleOp rescaleOp = new RescaleOp(1.5f, 0, null);
            BufferedImage contrastedImage = rescaleOp.filter(originalImage, null);

            BufferedImage grayImage = new BufferedImage(
                    contrastedImage.getWidth(),
                    contrastedImage.getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY
            );
            Graphics2D g = grayImage.createGraphics();
            g.drawImage(contrastedImage, 0, 0, null);
            g.dispose();

            Tesseract tesseract = new Tesseract();

            File tessDataFolder = new File("tessdata");
            tesseract.setDatapath(tessDataFolder.getAbsolutePath());


            tesseract.setLanguage("por");
            tesseract.setPageSegMode(3);

            String textoExtraido = tesseract.doOCR(grayImage);
            String textoLimpo = textoExtraido.replaceAll("[\\r\\n]+", "\n").replace("\"", "\\\"").trim();

            if (textoLimpo.isEmpty()) {
                textoLimpo = "Nenhum texto detectado";
            }
            String textoSimplificado = service.simplificarTexto(textoLimpo);

            Map<String, String> resposta = new HashMap<>();
            resposta.put("textoOriginal", textoLimpo);
            resposta.put("textoSimplificado", textoSimplificado);
            System.out.println("foi");
            return ResponseEntity.ok(resposta);

        } catch (TesseractException e) {
            return ResponseEntity.status(500).body("{\"erro\": \"Erro ao processar OCR: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"erro\": \"Erro ao ler imagem: " + e.getMessage() + "\"}");
        }
    }

    private BufferedImage corrigirOrientacao(BufferedImage image, MultipartFile file) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file.getInputStream());
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                int orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);

                switch (orientation) {
                    case 1: // Normal
                        return image;
                    case 2: // Flip horizontal
                        return flip(image, true, false);
                    case 3: // Rotate 180
                        return rotate(image, 180);
                    case 4: // Flip vertical
                        return flip(image, false, true);
                    case 5: // Transpose
                        return rotate(flip(image, true, false), 90);
                    case 6: // Rotate 90 CW
                        return rotate(image, 90);
                    case 7: // Transverse
                        return rotate(flip(image, true, false), 270);
                    case 8: // Rotate 270 CW
                        return rotate(image, 270);
                    default:
                        return image;
                }
            }
        } catch (Exception e) {
            // Não conseguiu ler EXIF, retorna imagem original
            return image;
        }
        return image;
    }

    private BufferedImage rotate(BufferedImage img, int degrees) {
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage rotatedImage;
        if (degrees == 90 || degrees == 270) {
            rotatedImage = new BufferedImage(h, w, img.getType());
        } else {
            rotatedImage = new BufferedImage(w, h, img.getType());
        }

        Graphics2D graphic = rotatedImage.createGraphics();
        graphic.rotate(Math.toRadians(degrees), rotatedImage.getWidth() / 2.0, rotatedImage.getHeight() / 2.0);

        int x = (rotatedImage.getWidth() - w) / 2;
        int y = (rotatedImage.getHeight() - h) / 2;
        graphic.drawImage(img, x, y, null);
        graphic.dispose();

        return rotatedImage;
    }

    private BufferedImage flip(BufferedImage img, boolean horizontal, boolean vertical) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage flipped = new BufferedImage(w, h, img.getType());
        Graphics2D g = flipped.createGraphics();
        g.drawImage(img, horizontal ? w : 0, vertical ? h : 0, horizontal ? -w : w, vertical ? -h : h, null);
        g.dispose();
        return flipped;
    }
}