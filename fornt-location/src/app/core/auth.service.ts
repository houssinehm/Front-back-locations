import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs';
import { environment } from '../../environment/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = environment.apiUrl + '/auth';

  constructor(private http: HttpClient) {}

  login(payload: { email: string; password: string }) {
    return this.http.post<any>(`${this.baseUrl}/login`, payload).pipe(
      map((response) => {
        const token = response.data;
        localStorage.setItem('token', token);
        return response;
      })
    );
  }

  register(payload: any) {
    console.log(payload);
    return this.http.post<any>(`${this.baseUrl}/register`, payload);
  }

  forgotPassword(email: string) {
    return this.http.post<any>(`${this.baseUrl}/forgot-password`, { email });
  }

  resetPassword(payload: { token: string; newPassword: string }) {
    return this.http.post<any>(`${this.baseUrl}/reset-password`, payload);
  }

  activateAccount(token: string) {
    return this.http.get<any>(`${this.baseUrl}/activate?token=${token}`);
  }

  logout() {
    localStorage.removeItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRoles(): string[] {
    const token = localStorage.getItem('token');
    if (!token) return [];

    const payload = token.split('.')[1];
    const decodedPayload = JSON.parse(atob(payload));
    return decodedPayload.roles || [];
  }

  isClient(): boolean {
    return this.getRoles().includes('ROLE_CLIENT');
  }


  isAgence(): boolean {
    return this.getRoles().includes('ROLE_AGENCE');
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const exp = payload.exp;
      return Date.now() < exp * 1000;
    } catch (e) {
      return false;
    }
  }

  getEmail() {
    const token = localStorage.getItem('token');
    if (!token) return [];

    const payload = token.split('.')[1];
    const decodedPayload = JSON.parse(atob(payload));
    return decodedPayload.sub || [];
  }
}
