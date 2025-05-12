import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { environment } from '../../environment/environment';
import { Observable } from 'rxjs';
import { Announce } from '../models/announce';

@Injectable({
  providedIn: 'root',
})
export class AnnonceService {
  private resourceUrl = environment.apiUrl + '/annonces';

  constructor(private http: HttpClient, private authService: AuthService) {}

  // 🔹 Create a new annonce
  create(announce: any): Observable<any> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json');

    return this.http.post(
      `${this.resourceUrl}/create`,
      JSON.stringify(announce),
      {
        headers,
      }
    );
  }

  // 🔹 Get all annonces of the connected agency
  getMyAnnonces(): Observable<Announce[]> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Announce[]>(`${this.resourceUrl}/mes-annonces`, {
      headers,
    });
  }

  // 🔹 Get available annonces (with date range and city)
  getAvailableAnnonces(
    start: string,
    end: string,
    city: string
  ): Observable<Announce[]> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const params = new HttpParams()
      .set('start', start)
      .set('end', end)
      .set('city', city);

    return this.http.get<Announce[]>(`${this.resourceUrl}/disponibles`, {
      headers,
      params,
    });
  }

  // 🔹 Update annonce
  update(
    id: number,
    data: {
      description?: string;
      dailyPrice?: number;
      cancelFree?: boolean;
      status?: boolean;
    }
  ): Observable<any> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.put(`${this.resourceUrl}/${id}`, data, { headers });
  }

  // 🔹 Delete annonce
  delete(id: number): Observable<any> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.delete(`${this.resourceUrl}/${id}`, { headers });
  }
  private buildFormData(announce: any): FormData {
    const formData = new FormData();

    formData.append('id_car', announce.id_car.toString());
    formData.append('daily_price', announce.daily_price.toString());
    formData.append('cancel_free', announce.cancel_free.toString());
    formData.append('description', announce.description);
    formData.append('status', announce.status.toString());

    return formData;
  }
}
