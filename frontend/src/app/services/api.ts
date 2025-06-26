
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'   
})
export class ApiService {
  private baseUrl = 'http://localhost:3000'; 

  constructor(private http: HttpClient) {}

  uploadDocument(form: FormData): Observable<{ originalText: string; simplifiedText: string }> {
    return this.http.post<{ originalText: string; simplifiedText: string }>(`${this.baseUrl}/upload`, form);
  }

  askDocument(question: string): Observable<{ answer: string }> {
    return this.http.post<{ answer: string }>(`${this.baseUrl}/ask`, { question });
  }
}
