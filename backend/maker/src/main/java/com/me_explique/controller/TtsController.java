package com.me_explique.controller;

import com.me_explique.service.TtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tts")
@CrossOrigin(origins = {"http://localhost:4200", "https://me-explique-git-main-galvessantos-projects.vercel.app"})
public class TtsController {

    @Autowired
    private TtsService ttsService;

    @PostMapping("/convert")
    public ResponseEntity<byte[]> convertTextToSpeech(@RequestBody String text) {
        try {
            byte[] audioBytes = ttsService.convertTextToSpeech(text);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("audio/mpeg"));
            headers.setContentDispositionFormData("attachment", "audio.mp3");
            headers.add("Content-Length", String.valueOf(audioBytes.length));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(audioBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/convert/custom")
    public ResponseEntity<byte[]> convertTextToSpeechCustom(@RequestBody TtsRequest request) {
        try {
            byte[] audioBytes = ttsService.convertTextToSpeechCustom(
                    request.getText(),
                    request.getVoiceType(),
                    request.getSpeakingRate(),
                    request.getPitch()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("audio/mpeg"));
            headers.setContentDispositionFormData("attachment", "audio.mp3");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(audioBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testTts() {
        try {
            byte[] testAudio = ttsService.convertTextToSpeech("Teste do sistema de síntese de voz para acessibilidade");
            return ResponseEntity.ok("TTS funcionando! Áudio gerado com " + testAudio.length + " bytes");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro no TTS: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<TtsStatus> getStatus() {
        return ResponseEntity.ok(new TtsStatus(
                ttsService.isGoogleCloudEnabled(),
                ttsService.isGoogleCloudEnabled() ? "Google Cloud TTS ativo" : "Modo desenvolvimento - configure GOOGLE_APPLICATION_CREDENTIALS"
        ));
    }

    @GetMapping("/voices")
    public ResponseEntity<TtsService.VoiceType[]> getAvailableVoices() {
        return ResponseEntity.ok(TtsService.VoiceType.values());
    }

    public static class TtsStatus {
        private boolean googleCloudEnabled;
        private String message;

        public TtsStatus(boolean googleCloudEnabled, String message) {
            this.googleCloudEnabled = googleCloudEnabled;
            this.message = message;
        }

        public boolean isGoogleCloudEnabled() { return googleCloudEnabled; }
        public void setGoogleCloudEnabled(boolean googleCloudEnabled) { this.googleCloudEnabled = googleCloudEnabled; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class TtsRequest {
        private String text;
        private TtsService.VoiceType voiceType = TtsService.VoiceType.FEMININE;
        private double speakingRate = 0.85;
        private double pitch = 0.0;

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public TtsService.VoiceType getVoiceType() { return voiceType; }
        public void setVoiceType(TtsService.VoiceType voiceType) { this.voiceType = voiceType; }

        public double getSpeakingRate() { return speakingRate; }
        public void setSpeakingRate(double speakingRate) { this.speakingRate = speakingRate; }

        public double getPitch() { return pitch; }
        public void setPitch(double pitch) { this.pitch = pitch; }
    }
}