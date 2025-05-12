import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../../core/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './forgot-password.component.html',
  
})
export class ForgotPasswordComponent {
  form: FormGroup;
  success = '';
  error = '';
  loading = false;
  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  

  get email() {
    return this.form.get('email');
  }
  submit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.success = '';
    this.error = '';
    this.authService
      .forgotPassword(this.form.value.email)
      .subscribe({
        next: () => {
          this.loading = false;
          this.success = 'Lien de réinitialisation envoyé.';
        },
        error: (err) => {
          this.loading = false;
          this.error = err.error?.message || 'Erreur lors de l’envoi';
        },
      });
  }
}
