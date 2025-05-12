import { Car } from './car';

export interface Announce {
  id?: number;
  description: string;
  daily_price: number;
  cancel_free: boolean;
  status: boolean;
  car: Car;
  grayscale?: boolean; // ajout de la propriété grayscale
  // Ajoute d'autres champs de voiture si nécessaire
}
