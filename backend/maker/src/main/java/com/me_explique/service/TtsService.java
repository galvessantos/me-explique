package com.me_explique.service;

import com.google.cloud.texttospeech.v1.*;
import com.me_explique.exception.TtsException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TtsService {

    private final TextToSpeechClient textToSpeechClient;

    public TtsService() {
        try {
            this.textToSpeechClient = TextToSpeechClient.create();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao inicializar Google Cloud TTS", e);
        }
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

        try {
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

        } catch (Exception e) {
            throw new TtsException("Erro ao converter texto em áudio: " + e.getMessage());
        }
    }
}