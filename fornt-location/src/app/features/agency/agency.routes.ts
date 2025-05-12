import { Routes } from '@angular/router';
import { AgencyLayoutComponent } from './components/agency-layout/agency-layout.component';

export const AGENCY_ROUTES: Routes = [
  {
    path: '',
    component: AgencyLayoutComponent,
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'my-cars',
        loadComponent: () => import('./pages/my-cars/my-cars.component').then(m => m.MyCarsComponent)
      },
      {
        path: 'published-ads',
        loadComponent: () => import('./pages/published-ads/published-ads.component').then(m => m.PublishedAdsComponent)
      },
      {
        path: 'reserved-cars',
        loadComponent: () => import('./pages/reserved-cars/reserved-cars.component').then(m => m.ReservedCarsComponent)
      },
      {
        path: 'account-settings',
        loadComponent: () => import('./pages/account-settings/account-settings.component').then(m => m.AccountSettingsComponent)
      }
    ]
  }
]; 