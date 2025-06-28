import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UploadCardComponent }          from '../../components/upload-card/upload-card';
import { OriginalTextCardComponent }    from '../../components/original-text-card/original-text-card';
import { SimplifiedTextCardComponent }  from '../../components/simplified-text-card/simplified-text-card';
import { TtsControlsComponent } from '../../components/tts-controls/tts-controls';
import { ApiService } from '../../services/api';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    UploadCardComponent,           
    OriginalTextCardComponent,
    SimplifiedTextCardComponent,
    TtsControlsComponent
  ],
  templateUrl: './home.html',
  styleUrls: ['./home.scss'],
})
export class HomeComponent {

  originalText = '';
  simplifiedText = '';
  isLoading = false;

  currentAudio: HTMLAudioElement | null = null;
  currentSpeed = 1.0;
  isUsingWebApi = false; 

  constructor(private api: ApiService) {}

  onFileUpload(file: File) {
    this.isLoading = true;
    this.originalText = 'Estamos fazendo o carregamento do seu texto...';
    this.simplifiedText = 'A versão simplificada está sendo preparada...';

    const formData = new FormData();
    formData.append('file', file); 

    this.api.uploadDocument(formData).subscribe({
      next: (res) => {
        this.originalText = res.textoOriginal;
        this.simplifiedText = res.textoSimplificado;
        this.isLoading = false;
        console.log('Upload realizado com sucesso:', res);
      },
      error: (err) => {
        console.error('Erro ao enviar imagem:', err);
        this.originalText = 'Erro ao processar a imagem';
        this.simplifiedText = 'Tente novamente com outra imagem';
        this.isLoading = false;
      }
    });
  }

  onPlayTts() {
    if (!this.simplifiedText || this.simplifiedText.includes('sendo preparada')) {
      console.log('Texto não está pronto para TTS');
      return;
    }

    this.stopCurrentAudio();

    if ('speechSynthesis' in window) {
      this.playWithWebSpeechAPI();
    } else {
      this.playWithGoogleCloudTTS();
    }
  }

  private playWithWebSpeechAPI() {
    this.isUsingWebApi = true;
    
    window.speechSynthesis.cancel();
    
    const utterance = new SpeechSynthesisUtterance(this.simplifiedText);
    utterance.lang = 'pt-BR';
    utterance.rate = this.currentSpeed;
    utterance.pitch = 1.0;
    utterance.volume = 1.0;

    utterance.onstart = () => console.log('TTS iniciado');
    utterance.onend = () => console.log('TTS finalizado');
    utterance.onerror = (event) => console.error('Erro no TTS:', event);

    window.speechSynthesis.speak(utterance);
  }

  private playWithGoogleCloudTTS() {
    this.isUsingWebApi = false;

    this.api.convertTextToSpeech(this.simplifiedText).subscribe({
      next: (audioBlob) => {
        const audioUrl = URL.createObjectURL(audioBlob);
        this.currentAudio = new Audio(audioUrl);
        
        this.currentAudio.playbackRate = this.currentSpeed;
        
        this.currentAudio.play().catch(err => {
          console.error('Erro ao reproduzir áudio:', err);
        });

        this.currentAudio.onended = () => {
          URL.revokeObjectURL(audioUrl);
          this.currentAudio = null;
        };
      },
      error: (err) => {
        console.error('Erro ao gerar áudio:', err);
        this.playWithWebSpeechAPI();
      }
    });
  }

  onPauseTts() {
    if (this.isUsingWebApi) {
      if (window.speechSynthesis.speaking && !window.speechSynthesis.paused) {
        window.speechSynthesis.pause();
      } else if (window.speechSynthesis.paused) {
        window.speechSynthesis.resume();
      }
    } else if (this.currentAudio) {
      if (this.currentAudio.paused) {
        this.currentAudio.play();
      } else {
        this.currentAudio.pause();
      }
    }
  }

  onStopTts() {
    this.stopCurrentAudio();
  }

  private stopCurrentAudio() {
    if (this.isUsingWebApi) {
      window.speechSynthesis.cancel();
    } else if (this.currentAudio) {
      this.currentAudio.pause();
      this.currentAudio.currentTime = 0;
      this.currentAudio = null;
    }
  }

  onSpeedChange(newSpeed: number) {
    this.currentSpeed = newSpeed;
    
    if (this.isUsingWebApi && window.speechSynthesis.speaking) {
      const wasPlaying = !window.speechSynthesis.paused;
      window.speechSynthesis.cancel();
      
      if (wasPlaying) {
        setTimeout(() => {
          this.playWithWebSpeechAPI();
        }, 100);
      }
    } 
    else if (this.currentAudio && !this.currentAudio.paused) {
      this.currentAudio.playbackRate = newSpeed;
    }
  }
}