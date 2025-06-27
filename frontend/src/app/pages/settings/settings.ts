// src/app/components/settings/settings.ts
import { Component, Inject } from '@angular/core';
import { CommonModule, DOCUMENT } from '@angular/common';
import { FormsModule }  from '@angular/forms';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [ CommonModule, FormsModule ],
  templateUrl: './settings.html',
  styleUrls:   ['./settings.scss'],
})
export class SettingsComponent {
  
  ocrLanguage         = 'pt';
  simplificationLevel = 'medium';
  theme: 'light' | 'dark' = 'light';


  languages = [
    { code: 'en', label: 'English'   },
    { code: 'pt', label: 'Português' },
    { code: 'es', label: 'Español'   }
  ];
  levels = [
    { code: 'low',    label: 'Baixa'  },
    { code: 'medium', label: 'Média'  },
    { code: 'high',   label: 'Alta'   }
  ];

  constructor(@Inject(DOCUMENT) private document: Document) {
    
    const savedLang  = localStorage.getItem('ocrLanguage');
    const savedLevel = localStorage.getItem('simplificationLevel');
    const savedTheme = localStorage.getItem('theme');

    if (savedLang)  this.ocrLanguage = savedLang;
    if (savedLevel) this.simplificationLevel = savedLevel as any;
    if (savedTheme === 'dark' || savedTheme === 'light') {
      this.theme = savedTheme;
      this.applyTheme();
    }
  }

  onSubmit() {
  
    localStorage.setItem('ocrLanguage', this.ocrLanguage);
    localStorage.setItem('simplificationLevel', this.simplificationLevel);
  
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
