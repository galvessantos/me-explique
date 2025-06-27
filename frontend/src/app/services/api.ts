import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = 'http://localhost:8080/api'; 

  constructor(private http: HttpClient) {}

  uploadDocument(form: FormData) {
    return this.http.post<{ textoOriginal: string; textoSimplificado: string }>(
      `${this.baseUrl}/ocr/ler-imagem`,
      form
    );

}
