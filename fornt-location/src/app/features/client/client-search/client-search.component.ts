import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, RouterOutlet } from '@angular/router';
import { Announce } from '../../../models/announce';
import { AnnonceService } from '../../../core/annonce.service';
import { CarService } from '../../../core/car.service';
import { NavbareComponent } from '../../public/components/header/header.component';
import { ClientNavbarComponent } from './client-navbar/client-navbar.component';
import { FiltersSidebarComponent } from './filters-sidebar/filters-sidebar.component';
import { CarListComponent } from './car-list/car-list.component';
import { FooterComponent } from '../../public/components/footer/footer.component';
import { CommonModule, NgClass, NgIf } from '@angular/common';

@Component({
  selector: 'app-client-search',
  standalone: true,
  imports: [
    NavbareComponent,
    ClientNavbarComponent,
    FiltersSidebarComponent,
    CarListComponent,
    FooterComponent,
    RouterOutlet,
    CommonModule
  ],
  templateUrl: './client-search.component.html',
  styleUrls: ['./client-search.component.css'],
})
export class ClientSearchComponent implements OnInit {
  annonces: Announce[] = [];
  isBookingRoute: boolean = false;  // Declare the variable

  constructor(
    private router: Router,
    private annonceService: AnnonceService,
    private carService: CarService
  ) {}

  ngOnInit(): void {
    // Check the current route and set isBookingRoute accordingly
    const currentRoute = this.router.url;
    this.isBookingRoute = currentRoute.includes('booking');  // Example check for 'booking' in the route

    const { start, end, city } = this.router.routerState.snapshot.root.queryParams;
    if (start && end && city) {
      this.annonceService
        .getAvailableAnnonces(start, end, city)
        .subscribe((data) => (this.annonces = data));
    }
  }
}
