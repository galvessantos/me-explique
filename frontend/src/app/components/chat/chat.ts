import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface Message {
  author: 'ExplicaMe' | 'User';
  text: string;
}

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.html',
  styleUrls: ['./chat.scss'],
})
export class ChatComponent {
  messages: Message[] = [
    { author: 'ExplicaMe', text: "Olá! Estou aqui para ajudar você a entender seu documento. Sinta-se à vontade para fazer quaisquer perguntas!" }
  ];
  newQuestion: string = '';

  send() {
    const text = this.newQuestion.trim();
    if (!text) return;

    
    this.messages.push({ author: 'User', text });
    this.newQuestion = '';

    
    setTimeout(() => {
      this.messages.push({
        author: 'ExplicaMe',
        text: 'Essa é uma resposta simulada à sua pergunta.'
      });
    }, 800);
  }
}
