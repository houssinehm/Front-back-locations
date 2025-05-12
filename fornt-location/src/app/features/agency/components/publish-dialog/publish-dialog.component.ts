import { Component, EventEmitter, Input, Output } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { Announce } from '../../../../models/announce';
import { Car } from '../../../../models/car';
import { HttpErrorResponse } from '@angular/common/http';
import { AnnonceService } from '../../../../core/annonce.service';

interface ResponseMessage {
  message: string;
}
@Component({
  selector: 'app-publish-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './publish-dialog.component.html',
  styleUrls: ['./publish-dialog.component.css'],
})
export class PublishDialogComponent {
  annonceForm: FormGroup;
  @Output() cancel = new EventEmitter<void>();
  @Output() publish = new EventEmitter<{
    description: string;
    price: number;
  }>();
  @Output() formSubmitted = new EventEmitter<void>();
  @Input() carId: number | null = null;
  annonces: Announce[] = [];
  car: Car[] = [];
  loading: boolean = false;

  description: string = '';
  price: number = 0;
  constructor(
    private fb: FormBuilder,
    private annonceService: AnnonceService,
    private router: Router
  ) {
    this.annonceForm = this.fb.group({
      description: ['', Validators.required],

      daily_price: ['', Validators.required],
      cancel_free: [false, Validators.requiredTrue],
    });
  }

  ngOnInit(): void {
    this.annonceService
      .getMyAnnonces()
      .subscribe((annonces: Announce[]) => (this.annonces = annonces));
  }
  submit(): void {
    console.log(this.annonceForm.invalid);
    if (this.annonceForm.invalid) return;

    this.loading = true;

    const formData = {
      description: this.annonceForm.get('description')?.value,
      daily_price: this.annonceForm.get('daily_price')?.value,
      cancel_free: true, // Valeur par défaut
      status: true, // Valeur par défaut
      id_car: this.carId,
    };
    console.log('Car ID:', this.carId);

    // Remplacer FormData par un objet simple pour envoyer en JSON
    this.annonceService.create(formData).subscribe({
      next: (res: ResponseMessage) => {
        this.loading = false;
        console.log(res.message);

        // Redirection vers la page des annonces publiées
        this.router.navigate(['/agency/published-ads']);
      },
      error: (err: HttpErrorResponse) => {
        this.loading = false;
        // Afficher un message d'erreur détaillé
        console.error("Une erreur s'est produite:", err);
        alert(
          "Une erreur est survenue lors de la création de l'annonce. Veuillez réessayer."
        );
      },
    });
  }

  onCancel() {
    this.cancel.emit();
  }

  onPublish() {
    this.publish.emit({
      description: this.annonceForm.get('description')?.value,
      price: this.annonceForm.get('price')?.value,
    });
  }
}
