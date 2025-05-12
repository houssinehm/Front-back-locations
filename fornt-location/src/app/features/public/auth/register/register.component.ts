import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../../../core/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  activeTab: string = 'client';
  clientForm: FormGroup = this.initClientForm();
  agencyForm: FormGroup = this.initAgencyForm();
  form!: FormGroup;
  error = '';
  success = '';
  loading = false;

  constructor(private fb: FormBuilder, private authService: AuthService) {}

  private initClientForm(): FormGroup {
    return this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      age: ['', [Validators.required, Validators.min(18)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      phone: ['', Validators.required],
    });
  }

  private initAgencyForm(): FormGroup {
    return this.fb.group({
      agencyName: ['', Validators.required],
      numberOfCars: ['', [Validators.required, Validators.min(1)]],
      address: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      phone: ['', Validators.required],
    });
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  onSubmit() {
    this.loading = true;
    this.error = '';
    this.success = '';

    if (this.activeTab === 'client') {
      if (this.clientForm.valid) {
        const payload = {
          email: this.clientForm.value.email,
          password: this.clientForm.value.password,
          telephone: this.clientForm.value.phone,
          first_name: this.clientForm.value.firstName,
          name: this.clientForm.value.lastName,
          age: this.clientForm.value.age,
          roles: ['ROLE_CLIENT'],
        };

        console.log('Client form submitted:', payload);

        this.authService.register(payload).subscribe({
          next: (res) => {
            this.loading = false;
            this.success = 'Inscription réussie ! Vérifiez votre email.';
          },
          error: (err) => {
            this.loading = false;
            this.error = err.error?.message || 'Échec de l’inscription';
          },
        });
      } else {
        this.clientForm.markAllAsTouched();
        this.loading = false;
      }
    } else {
      if (this.agencyForm.valid) {
        const payload = {
          email: this.agencyForm.value.email,
          password: this.agencyForm.value.password,
          telephone: this.agencyForm.value.phone,
          nom_agence:this.agencyForm.value.agencyName,
          nombre_voiture:this.agencyForm.value.numberOfCars,
          adresse:this.agencyForm.value.address,
          roles: ['ROLE_AGENCE'], // 👈 Add the agency role
        };

        console.log('Agency form submitted:', payload);

        this.authService.register(payload).subscribe({
          next: (res) => {
            this.loading = false;
            this.success = 'Inscription réussie ! Vérifiez votre email.';
          },
          error: (err) => {
            this.loading = false;
            this.error = err.error?.message || 'Échec de l’inscription';
          },
        });
      } else {
        this.agencyForm.markAllAsTouched();
        this.loading = false;
      }
    }
  }
}
