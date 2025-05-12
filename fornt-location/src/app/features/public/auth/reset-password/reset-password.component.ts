import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../../core/auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css',
})
export class ResetPasswordComponent {
  form: FormGroup;
  token = '';
  success = '';
  error = '';
  loading = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {
    this.form = this.fb.group(
      {
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', Validators.required],
      },
      { validators: this.passwordMatchValidator }
    );


    this.token = this.route.snapshot.queryParamMap.get('token') || '';
  }

  private initForm(): FormGroup {
    return this.fb.group(
      {
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', Validators.required],
      },
      { validators: this.passwordMatchValidator }
    );
  }

  private passwordMatchValidator(form: FormGroup) {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');

    if (password?.value !== confirmPassword?.value) {
      return { passwordMismatch: true };
    }
    return null;
  }

  get password() {
    return this.form.get('password');
  }

  get confirmPassword() {
    return this.form.get('confirmPassword');
  }

  submit() {
    if (!this.token || this.form.invalid) return;
    this.loading = true;
    this.success = '';
    this.error = '';
    this.authService
      .resetPassword({
        token: this.token,
        newPassword: this.form.value.password,
      })
      .subscribe({
        next: () => {
          this.loading = false;
          this.success = 'Mot de passe réinitialisé !';
          setTimeout(() => this.router.navigate(['/auth/login']), 3000);
        },
        error: (err) => {
          this.loading = false;
          this.error = err.error?.message || 'Échec de la réinitialisation';
        },
      });
  }
}
