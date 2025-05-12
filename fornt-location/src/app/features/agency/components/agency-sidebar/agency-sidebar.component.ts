import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-agency-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './agency-sidebar.component.html',
  styleUrls: ['./agency-sidebar.component.css']
})
export class AgencySidebarComponent {
  isCollapsed = false;
  @Output() onCollapse = new EventEmitter<boolean>();

  constructor(public router: Router) {}

  toggleCollapse() {
    this.isCollapsed = !this.isCollapsed;
    this.onCollapse.emit(this.isCollapsed);
  }
} 