import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = 'https://sua-api.com'; // ajuste

  constructor(private http: HttpClient) {}

  uploadDocument(form: FormData) {
    return this.http.post<{ originalText: string; simplifiedText: string }>(
      `${this.baseUrl}/upload`,
      form
    );
  }
}
