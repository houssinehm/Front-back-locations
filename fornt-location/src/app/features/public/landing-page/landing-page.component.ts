import { Component } from '@angular/core';
import { NavbareComponent } from "../components/header/header.component";
import { HeroSectionComponent } from "../components/hero-section/hero-section.component";
import { CardsComponent } from "../components/cards/cards.component";
import { StepsComponent } from "../components/steps/steps.component";
import { AboutUsComponent } from "../components/about-us/about-us.component";
import { StatsComponent } from "../components/stats/stats.component";
import { AgencyRegisterComponent } from "../components/agency-register/agency-register.component";
import { FooterComponent } from "../components/footer/footer.component";


@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [NavbareComponent, HeroSectionComponent, CardsComponent, StepsComponent, AboutUsComponent, StatsComponent, AgencyRegisterComponent, FooterComponent],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css'
})
export class LandingPageComponent {

}
