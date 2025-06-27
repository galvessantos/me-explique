import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-simplified-text-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './simplified-text-card.html',
  styleUrls: ['./simplified-text-card.scss'],
})
export class SimplifiedTextCardComponent {
  
  @Input() text: string = '';
}
