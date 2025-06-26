import { Component, Inject } from '@angular/core';
import { CommonModule, DOCUMENT } from '@angular/common';
import { FormsModule }  from '@angular/forms';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './settings.html',
  styleUrls: ['./settings.scss'],
})
export class SettingsComponent {
  ocrLanguage = 'en';
  simplificationLevel = 'medium';

  theme: 'light' | 'dark' = 'light';

  languages = [
    { code: 'en', label: 'English' },
    { code: 'pt', label: 'Português' },
    { code: 'es', label: 'Español' }
  ];
  levels = [
    { code: 'low',    label: 'Baixa' },
    { code: 'medium', label: 'Média' },
    { code: 'high',   label: 'Alta' }
  ];

  constructor(@Inject(DOCUMENT) private document: Document) {
    // Recuperar tema salvo
    const saved = localStorage.getItem('theme');
    if (saved === 'dark' || saved === 'light') {
      this.theme = saved;
      this.applyTheme();
    }
  }

  save() {
    // ... existing save for OCR & simplification ...
    this.saveTheme();
    alert('Configurações salvas com sucesso!');
  }

  toggleTheme(value: 'light' | 'dark') {
    this.theme = value;
    this.applyTheme();
  }

  private applyTheme() {
    this.document.body.classList.toggle('dark', this.theme === 'dark');
  }

  private saveTheme() {
    localStorage.setItem('theme', this.theme);
  }
}
