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

    if (this.currentAudio) {
      this.currentAudio.pause();
      this.currentAudio = null;
    }

    this.api.convertTextToSpeech(this.simplifiedText).subscribe({
      next: (audioBlob) => {
        const audioUrl = URL.createObjectURL(audioBlob);
        this.currentAudio = new Audio(audioUrl);
        this.currentAudio.play().catch(err => {
          console.error('Erro ao reproduzir áudio:', err);
        });
      },
      error: (err) => {
        console.error('Erro ao gerar áudio:', err);
      }
    });
  }

  onPauseTts() {
    if (this.currentAudio) {
      if (this.currentAudio.paused) {
        this.currentAudio.play();
      } else {
        this.currentAudio.pause();
      }
    }
  }

  onStopTts() {
    if (this.currentAudio) {
      this.currentAudio.pause();
      this.currentAudio.currentTime = 0;
    }
  }
}