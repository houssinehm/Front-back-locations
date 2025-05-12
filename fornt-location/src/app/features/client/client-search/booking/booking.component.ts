import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavbareComponent } from "../../../public/components/header/header.component";
import { FooterComponent } from "../../../public/components/footer/footer.component";

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [CommonModule, RouterModule, NavbareComponent, FooterComponent],
  templateUrl: './booking.component.html',
  styleUrl: './booking.component.css'
})
export class BookingComponent {
annonce: any;

}
