import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AddCarComponent } from '../add-car/add-car.component';
import { PublishDialogComponent } from '../../components/publish-dialog/publish-dialog.component';
import { Car } from '../../../../models/car';
import { CarService } from '../../../../core/car.service';
import { EditCarComponent } from '../edit-car/edit-car.component';

@Component({
  selector: 'app-my-cars',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    AddCarComponent,
    PublishDialogComponent,
    EditCarComponent,
  ],
  templateUrl: './my-cars.component.html',
  styleUrls: ['./my-cars.component.css'],
})
export class MyCarsComponent implements OnInit {
  cars: Car[] = [];
  showAddCarForm = false;
  showPublishDialog = false;
  selectedCarId: number | null = null;
  selectedCarForEdit: Car | null = null;

  constructor(private carService: CarService, private router: Router) {}

  ngOnInit(): void {
    this.loadCars();
  }

  toggleAddCarForm(): void {
    this.showAddCarForm = !this.showAddCarForm;
  }

  openPublishDialog(carId: number): void {
    this.selectedCarId = carId;
    this.showPublishDialog = true;
    console.log('Selected Car ID:', carId); // Affiche l'ID dans la console
  }

  closePublishDialog(): void {
    this.showPublishDialog = false;
    this.selectedCarId = null;
    
  }

  loadCars(): void {
    this.carService.getMyCars().subscribe((data) => (this.cars = data));
  }

  handlePublish(data: { description: string; price: number }): void {
    console.log('Publishing car:', this.selectedCarId, data);
    this.closePublishDialog();
    // Tu peux ajouter ici l'appel à un service pour publier l'annonce, exemple :
    // this.carService.publishCar(this.selectedCarId, data).subscribe(...)
  }

  deleteCar(carId: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette voiture ?')) {
      this.carService.delete(carId).subscribe(
        (response) => {
          // Retirer la voiture supprimée de la liste
          this.cars = this.cars.filter((car) => car.id !== carId);
          alert('Voiture supprimée avec succès');
        },
        (error) => {
          alert('Une erreur est survenue lors de la suppression de la voiture');
        }
      );
    }
  }

  openEditCarForm(car: Car): void {
    this.selectedCarForEdit = car;
  }

  closeEditCarForm(): void {
    this.selectedCarForEdit = null;
  }
}
