import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { LoginComponent } from './login/login.component';
import { FooterComponent } from "../components/footer/footer.component";
import { NavbareComponent } from "../components/header/header.component";


@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, LoginComponent, FooterComponent, NavbareComponent],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent {

}
