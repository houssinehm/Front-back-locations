import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Announce } from '../../../../models/announce';
import { AnnonceService } from '../../../../core/annonce.service';
import { Router } from '@angular/router';
import { CarService } from '../../../../core/car.service';

@Component({
  selector: 'app-published-ads',
  templateUrl: './published-ads.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule],
})
export class PublishedAdsComponent {
  annonces: Announce[] = [];
  isGrayscale: boolean[] = []; // Un tableau pour gérer dynamiquement les cases à cocher
  isGrayscale1 = false;
  isGrayscale2 = false;
  isGrayscale3 = false;
  constructor(
    private annonceService: AnnonceService,
    private router: Router,
    private carService: CarService
  ) {}

  ngOnInit(): void {
    this.loadAnnonces();
  }

  loadAnnonces() {
    this.annonceService.getMyAnnonces().subscribe((annonces) => {
      this.annonces = annonces.map((annonce) => ({
        ...annonce,
        grayscale: false, // ajoute cette propriété manuellement
      }));
      this.isGrayscale = this.annonces.map(() => false); // Initialisation dynamique du tableau isGrayscale

      for (let annonce of this.annonces) {
        if (annonce.car && typeof annonce.car.id === 'number') {
          this.carService.getCarById(annonce.car.id).subscribe((car) => {
            annonce.car = car;
          });
        }
      }
    });
  }
  toggleGrayscale(index: number) {
    this.isGrayscale[index] = !this.isGrayscale[index]; // Inverse l'état du grayscale pour l'image correspondante
  }
}

