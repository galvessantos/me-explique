import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
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

  save() {
   
    console.log('Configurações salvas:', {
      ocrLanguage: this.ocrLanguage,
      simplificationLevel: this.simplificationLevel
    });
    alert('Configurações salvas com sucesso!');
  }
}
