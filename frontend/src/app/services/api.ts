import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = 'https://me-explique.onrender.com/api'; 

  constructor(private http: HttpClient) {}

  uploadDocument(form: FormData): Observable<{ textoOriginal: string; textoSimplificado: string }> {
    return this.http.post<{ textoOriginal: string; textoSimplificado: string }>(
      `${this.baseUrl}/ocr/ler-imagem`,
      form
    );
  }

  simplifyText(text: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/simplificar`, text, {
      headers: { 'Content-Type': 'application/json' },
      responseType: 'text'
    });
  }

  convertTextToSpeech(text: string): Observable<Blob> {
    return this.http.post(`${this.baseUrl}/tts/convert`, `"${text}"`, {
      headers: { 'Content-Type': 'application/json' },
      responseType: 'blob'
    });
  }

  getTtsStatus(): Observable<{ googleCloudEnabled: boolean; message: string }> {
    return this.http.get<{ googleCloudEnabled: boolean; message: string }>(
      `${this.baseUrl}/tts/status`
    );
  }
}