import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-agency-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './agency-layout.component.html',
  styles: []
})
export class AgencyLayoutComponent {
  isCollapsed = false;
  isMobileMenuOpen = false;

  constructor(private router: Router) {}

  isActive(route: string): boolean {
    return this.router.url.includes(route);
  }
} 