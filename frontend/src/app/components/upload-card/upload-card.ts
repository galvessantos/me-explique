import { Component, ElementRef, EventEmitter, Output, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService }   from '../../services/api';

@Component({
  selector: 'app-upload-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './upload-card.html',
  styleUrls: ['./upload-card.scss'],
})
export class UploadCardComponent {
  @Output() fileUploaded = new EventEmitter<{ original: string; simplified: string }>();

  constructor(private api: ApiService) {}

  onDragOver(evt: DragEvent) { evt.preventDefault(); }
  onDrop(evt: DragEvent) {
    evt.preventDefault();
    if (evt.dataTransfer?.files.length) this.uploadFile(evt.dataTransfer.files[0]);
  }
  onFileSelected(evt: Event) {
    const file = (evt.target as HTMLInputElement).files?.[0];
    if (file) this.uploadFile(file);
  }
  private uploadFile(file: File) {
    const form = new FormData();
    form.append('file', file);
    this.api.uploadDocument(form).subscribe({
      next: res => this.fileUploaded.emit({ original: res.originalText, simplified: res.simplifiedText }),
      error: err => console.error(err),
    });
  }
}
