import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule }  from '@angular/forms';

@Component({
  selector: 'app-tts-controls',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tts-controls.html',
  styleUrls: ['./tts-controls.scss'],
})
export class TtsControlsComponent {
  @Output() play  = new EventEmitter<void>();
  @Output() pause = new EventEmitter<void>();

  speed = 1.0;

  onPlay() {
    this.play.emit();
  }

  onPause() {
    this.pause.emit();
  }
}
