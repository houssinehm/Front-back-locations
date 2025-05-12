import { Routes } from '@angular/router';

import { LandingPageComponent } from './features/public/landing-page/landing-page.component';
import { ClientSearchComponent } from './features/client/client-search/client-search.component';


export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/public/landing-page/landing-page.component').then(
        (m) => m.LandingPageComponent
      ),
  },
  {
    path: 'client-search',
    loadComponent: () =>
      import('./features/client/client-search/client-search.component').then(
        (m) => m.ClientSearchComponent
      ),
  },
  {
    path: 'client-search/booking/:id',
    loadComponent: () =>
      import('./features/client/client-search/booking/booking.component').then(
        (m) => m.BookingComponent
      ),
  },
  {
    path: 'auth',
    loadChildren: () =>
      import('./features/public/auth/auth.routes').then((m) => m.AUTH_ROUTES),
  },
  {
    path: 'agency',
    loadChildren: () =>
      import('./features/agency/agency.routes').then((m) => m.AGENCY_ROUTES),
  },
];
