export interface Car {
  id: number;
  city: string; // Par exemple, le nom de la ville ou un identifiant
  type: string; // Par exemple, le type de la voiture
  fuel: string; // Par exemple, "Essence", "Diesel", etc.
  gearbox: string; // Par exemple, "Manuelle", "Automatique"
  number_place: number;
  number_door: number;
  clim: boolean;
  number_bag: number;
  caution: number;
  url_photo: string;
  age_min: number;
  driving_license_min_year: number;
  min_age:number;
  model: string;
  brand: string; // Par exemple, le nom de la marque
  photoFile: File | null;
}
