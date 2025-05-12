import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // <= important !
import { Announce } from '../../../../models/announce';
import { AnnonceService } from '../../../../core/annonce.service';
import { Router } from '@angular/router';
import { CarService } from '../../../../core/car.service';

@Component({
  selector: 'app-hero-section',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './hero-section.component.html',
  styleUrl: './hero-section.component.css',
})
export class HeroSectionComponent {
  annonces: Announce[] = [];

  search = {
    dateStart: '',
    dateEnd: '',
    city: '',
  };

  constructor(
    private annonceService: AnnonceService,
    private router: Router,
    private carService: CarService
  ) {}

  onSearch() {
    const { dateStart, dateEnd, city } = this.search;

    if (!dateStart || !dateEnd || !city) {
      alert('Tous les champs sont requis.');
      return;
    }

    this.router.navigate(['/client-search'], {
      queryParams: {
        start: dateStart,
        end: dateEnd,
        city,
      },
    });
  }
}
