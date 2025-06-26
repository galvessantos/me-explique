import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UploadCardComponent }          from '../../components/upload-card/upload-card';
import { OriginalTextCardComponent }    from '../../components/original-text-card/original-text-card';
import { SimplifiedTextCardComponent }  from '../../components/simplified-text-card/simplified-text-card';
import { ChatComponent } from '../../components/chat/chat';
import { TtsControlsComponent } from '../../components/tts-controls/tts-controls';
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    UploadCardComponent,           
    OriginalTextCardComponent,
    SimplifiedTextCardComponent,
    ChatComponent,
    TtsControlsComponent
   
  ],
  templateUrl: './home.html',
  styleUrls: ['./home.scss'],
})
export class HomeComponent {
onPauseTts() {
throw new Error('Method not implemented.');
}
onPlayTts() {
throw new Error('Method not implemented.');
}
  originalText   = '';
  simplifiedText = '';

  onFileUploaded(e: { original: string; simplified: string }) {
    this.originalText   = e.original;
    this.simplifiedText = e.simplified;
  }
}
