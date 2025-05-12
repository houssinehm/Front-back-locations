import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-filters-sidebar',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './filters-sidebar.component.html',
  styleUrl: './filters-sidebar.component.css'
})
export class FiltersSidebarComponent {
  brands = ['Toyota', 'Honda', 'BMW', 'Audi', 'Mercedes', 'Ford'];
  types = ['Sedan', 'SUV', 'Coupe'];
  fuelTypes = ['Hybrid', 'Petrol', 'Diesel'];
  selectedBrand: string = '';
  selectedType: string = '';
  selectedFuelType: string = '';
  selectedTransmission: string = '';
  selectedClimate: string = '';

  ngOnInit() {
    console.log('Brands:', this.brands);
    console.log('Types:', this.types);
    console.log('Fuel Types:', this.fuelTypes);
  }
}
