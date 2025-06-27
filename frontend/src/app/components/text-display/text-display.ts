import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OriginalTextCardComponent }   from '../original-text-card/original-text-card';
import { SimplifiedTextCardComponent } from '../simplified-text-card/simplified-text-card';
import { TtsControlsComponent }        from '../tts-controls/tts-controls';

@Component({
  selector: 'app-text-display',
  standalone: true,
  imports: [
    CommonModule,
    OriginalTextCardComponent,
    SimplifiedTextCardComponent,
    TtsControlsComponent
  ],
  templateUrl: './text-display.html',
  styleUrls:   ['./text-display.scss']
})
export class TextDisplayComponent {
  originalText   = 'Estamos fazendo o carregamento do seu texto …';
  simplifiedText = 'A versão simplificada está sendo preparada …';

  speed     = 1.0;
  utterance = new SpeechSynthesisUtterance();

  speak() {
    window.speechSynthesis.cancel();
    this.utterance.text = this.simplifiedText;
    this.utterance.rate = this.speed;
    window.speechSynthesis.speak(this.utterance);
  }

  pauseSpeech() {
    window.speechSynthesis.pause();
  }

  stopSpeech() {
    window.speechSynthesis.cancel();
  }

  adjustRate(newRate: number) {
    this.speed = newRate;
    this.utterance.rate = newRate;
  }
}
