package com.me_explique.service;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.me_explique.exception.TtsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Service
public class TtsService {

    private static final Logger logger = LoggerFactory.getLogger(TtsService.class);
    private final TextToSpeechClient textToSpeechClient;
    private final boolean isGoogleCloudAvailable;

    @Value("${google.credentials.json:}")
    private String googleCredentialsJson;

    public TtsService() {
        TextToSpeechClient client = null;
        boolean available = false;

        try {
            String credentialsBase64 = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_BASE64");

            if (credentialsBase64 != null && !credentialsBase64.trim().isEmpty()) {
                byte[] decodedBytes = Base64.getDecoder().decode(credentialsBase64);
                String credentialsJson = new String(decodedBytes);

                ServiceAccountCredentials credentials = ServiceAccountCredentials
                        .fromStream(new ByteArrayInputStream(credentialsJson.getBytes()));

                client = TextToSpeechClient.create(
                        TextToSpeechSettings.newBuilder()
                                .setCredentialsProvider(() -> credentials)
                                .build()
                );
                available = true;
                logger.info("Google Cloud TTS iniciado com sucesso via Base64 env var");
            } else {
                client = TextToSpeechClient.create();
                available = true;
                logger.info("Google Cloud TTS iniciado com sucesso via credenciais padrão");
            }
        } catch (Exception e) {
            logger.warn("Google Cloud TTS não disponível. Usando modo mock. Erro: {}", e.getMessage());
        }

        this.textToSpeechClient = client;
        this.isGoogleCloudAvailable = available;
    }

    public enum VoiceType {
        FEMININE("pt-BR-Standard-A", SsmlVoiceGender.FEMALE),
        MASCULINE("pt-BR-Standard-B", SsmlVoiceGender.MALE),
        NEUTRAL("pt-BR-Wavenet-A", SsmlVoiceGender.FEMALE);

        private final String voiceName;
        private final SsmlVoiceGender gender;

        VoiceType(String voiceName, SsmlVoiceGender gender) {
            this.voiceName = voiceName;
            this.gender = gender;
        }

        public String getVoiceName() { return voiceName; }
        public SsmlVoiceGender getGender() { return gender; }
    }

    public byte[] convertTextToSpeech(String text) {
        return convertTextToSpeechCustom(text, VoiceType.FEMININE, 0.85, 0.0);
    }

    public byte[] convertTextToSpeechCustom(String text, VoiceType voiceType, double speakingRate, double pitch) {
        if (text == null || text.trim().isEmpty()) {
            throw new TtsException("Texto não pode estar vazio");
        }

        if (!isGoogleCloudAvailable) {
            return generateMockAudio(text, voiceType, speakingRate, pitch);
        }

        try {
            return generateRealAudio(text, voiceType, speakingRate, pitch);
        } catch (Exception e) {
            logger.warn("Erro no Google Cloud TTS, usando fallback: {}", e.getMessage());
            return generateMockAudio(text, voiceType, speakingRate, pitch);
        }
    }

    private byte[] generateRealAudio(String text, VoiceType voiceType, double speakingRate, double pitch) {
        SynthesisInput input = SynthesisInput.newBuilder()
                .setText(text)
                .build();

        VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                .setLanguageCode("pt-BR")
                .setName(voiceType.getVoiceName())
                .setSsmlGender(voiceType.getGender())
                .build();

        AudioConfig audioConfig = AudioConfig.newBuilder()
                .setAudioEncoding(AudioEncoding.MP3)
                .setSpeakingRate(speakingRate)
                .setPitch(pitch)
                .build();

        SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(
                input, voice, audioConfig);

        return response.getAudioContent().toByteArray();
    }

    private byte[] generateMockAudio(String text, VoiceType voiceType, double speakingRate, double pitch) {
        String mockContent = String.format(
                "MODO DESENVOLVIMENTO - TTS Mock Audio\n" +
                        "Texto: %s\n" +
                        "Voz: %s\n" +
                        "Velocidade: %.2f\n" +
                        "Tom: %.2f\n" +
                        "Para habilitar TTS real, configure GOOGLE_APPLICATION_CREDENTIALS_BASE64",
                text, voiceType.getVoiceName(), speakingRate, pitch
        );

        return mockContent.getBytes();
    }

    public boolean isGoogleCloudEnabled() {
        return isGoogleCloudAvailable;
    }
}