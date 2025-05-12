import { Component, Input, OnInit } from '@angular/core';
import { Announce } from '../../../../models/announce';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AnnonceService } from '../../../../core/annonce.service';
import { CarService } from '../../../../core/car.service';

@Component({
  selector: 'app-car-list',
  standalone: true,
  imports: [CommonModule,RouterLink],
  templateUrl: './car-list.component.html',
  styleUrl: './car-list.component.css',
})
export class CarListComponent {
  @Input() annonces: Announce[] = [];



}
