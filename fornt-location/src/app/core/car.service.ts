import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { environment } from '../../environment/environment';
import { Observable } from 'rxjs';
import { Car } from '../models/car';

@Injectable({
  providedIn: 'root',
})
export class CarService {
  private ressourceUrl = environment.apiUrl + '/agency';

  constructor(private http: HttpClient, private authService: AuthService) {}

  // Get all cars of the agency
  getMyCars(): Observable<Car[]> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Car[]>(`${this.ressourceUrl}/mes-voitures`, {
      headers,
    });
  }

  // Get a car by ID
  getCarById(id: number): Observable<Car> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Car>(`${this.ressourceUrl}/my-cars/${id}`, {
      headers,
    });
  }

  // Create a new car
  create(car: any): Observable<any> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const formData = car instanceof FormData ? car : this.buildFormData(car);
    return this.http.post(`${this.ressourceUrl}/create`, formData, {
      headers,
    });
  }

  // Helper method to build the FormData for creating a car
  private buildFormData(car: any): FormData {
    const formData = new FormData();
    formData.append('city', car.city);
    formData.append('type', car.type);
    formData.append('fuel', car.fuel);
    formData.append('gearbox', car.gearbox);
    formData.append('number_place', car.number_place.toString());
    formData.append('number_door', car.number_door.toString());
    formData.append('clim', car.clim.toString());
    formData.append('number_bag', car.number_bag?.toString());
    formData.append('caution', car.caution.toString());
    formData.append('age_min', car.age_min.toString());
    formData.append(
      'driving_license_min_year',
      car.driving_license_min_year?.toString() || '0'
    );
    formData.append('model', car.model || '0');
    formData.append('brand', car.brand);

    if (car.photoFile && car.photoFile instanceof File) {
      formData.append('photoFile', car.photoFile);
    } else {
      throw new Error('Une photo est obligatoire pour créer une voiture');
    }

    return formData;
  }

  // Update car details
  update(id: number, car: any): Observable<any> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const formData = this.buildFormDataForUpdate(car);
    return this.http.put(
      `${this.ressourceUrl}/my-cars/update/${id}`,
      formData,
      { headers }
    );
  }

  // Helper method to build the FormData for updating a car
  private buildFormDataForUpdate(car: any): FormData {
    const formData = new FormData();
    formData.append('city', car.city);
    formData.append('type', car.type);
    formData.append('fuel', car.fuel);
    formData.append('gearbox', car.gearbox);
    formData.append('number_place', car.number_place.toString());
    formData.append('number_door', car.number_door.toString());
    formData.append('clim', car.clim.toString());
    formData.append('number_bag', car.number_bag.toString());
    formData.append('caution', car.caution.toString());
    formData.append('age_min', car.age_min.toString());
    formData.append(
      'driving_license_min_year',
      car.driving_license_min_year.toString()
    );
    formData.append('model', car.model);
    formData.append('brand', car.brand);

    if (car.photoFile && car.photoFile instanceof File) {
      formData.append('photoFile', car.photoFile);
    } else if (car.url_photo) {
      formData.append('url_photo', car.url_photo); // If no new photo is provided
    }

    return formData;
  }

  // Delete a car by ID
  delete(id: number): Observable<any> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.delete(`${this.ressourceUrl}/${id}`, { headers });
  }

  // Get cars by city
  getCarsByCity(city: string): Observable<Car[]> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Car[]>(`${this.ressourceUrl}/my-cars/city/${city}`, {
      headers,
    });
  }

  // Get cars by brand
  getCarsByBrand(brand: string): Observable<Car[]> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Car[]>(`${this.ressourceUrl}/my-cars/brand/${brand}`, {
      headers,
    });
  }
}
