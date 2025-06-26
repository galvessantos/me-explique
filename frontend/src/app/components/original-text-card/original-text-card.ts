import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-original-text-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './original-text-card.html',
  styleUrls: ['./original-text-card.scss'],
})
export class OriginalTextCardComponent {
  
  @Input() text: string = '';
}
