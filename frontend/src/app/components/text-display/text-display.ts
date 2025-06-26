import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-text-display',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './text-display.html',
  styleUrls: ['./text-display.scss'],
})
export class TextDisplayComponent {
  @Input() originalText = '';
  @Input() simplifiedText = '';
}
