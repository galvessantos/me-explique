import {
  Component,
  ElementRef,
  EventEmitter,
  Output,
  ViewChild,
} from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-upload-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './upload-card.html',
  styleUrls: ['./upload-card.scss'],
})
export class UploadCardComponent {
  @ViewChild('fileInput', { read: ElementRef })
  fileInput!: ElementRef<HTMLInputElement>;

  @Output()
  fileDropped = new EventEmitter<File>();

  /** Abre o file picker */
  selectFile() {
    this.fileInput.nativeElement.click();
  }

  /** Quando o usuário arrasta algo sobre a área */
  onDragOver(evt: DragEvent) {
    evt.preventDefault();
    evt.stopPropagation();
    evt.dataTransfer!.dropEffect = 'copy';
  }

  /** Quando o usuário solta o arquivo */
  onDrop(evt: DragEvent) {
    evt.preventDefault();
    evt.stopPropagation();
    const files = evt.dataTransfer?.files;
    if (files && files.length > 0) {
      this.fileDropped.emit(files[0]);
    }
  }

  /** Quando o usuário escolhe via file picker */
  onFileSelected(evt: Event) {
    const input = evt.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.fileDropped.emit(input.files[0]);
    }
  }
}
