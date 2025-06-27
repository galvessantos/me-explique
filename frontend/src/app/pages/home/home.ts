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

originalText  = '';
simplifiedText = '';

constructor(private api: ApiService) {}

onFileUpload(file: File) {
    const formData = new FormData();
    formData.append('file', file);

    this.api.uploadDocument(formData).subscribe({
      next: (res) => {
        this.originalText = res.textoOriginal;
        this.simplifiedText = res.textoSimplificado;
        console.log(this.simplifiedText)
      },
      error: (err) => {
        console.error('Erro ao enviar imagem:', err);
      }
    });
  }
onPauseTts() {
throw new Error('Method not implemented.');
}
onPlayTts() {
throw new Error('Method not implemented.');
}
  
  onFileUploaded(e: { original: string; simplified: string }) {
    this.originalText   = e.original;
    this.simplifiedText = e.simplified;
  }
}
