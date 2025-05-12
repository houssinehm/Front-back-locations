import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { CarService } from '../../../../core/car.service';
import { Router } from '@angular/router';

interface FileWithPreview {
  file: File;
  preview: string;
  name: string;
}

@Component({
  selector: 'app-add-car',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-car.component.html',
})
export class AddCarComponent {
  carForm: FormGroup;
  @Output() formSubmitted = new EventEmitter<void>();
  selectedFiles: FileWithPreview[] = [];
  loading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private carService: CarService,
    private router: Router
  ) {
    this.carForm = this.fb.group({
      city: ['', Validators.required],
      brand: ['', Validators.required],
      model: ['', Validators.required],
      type: ['', Validators.required],
      fuel: ['', Validators.required],
      gearbox: ['', Validators.required],
      number_place: [
        '',
        [Validators.required, Validators.min(1), Validators.max(15)],
      ],
      number_door: [
        '',
        [Validators.required, Validators.min(2), Validators.max(10)],
      ],
      driving_license_min_year: [
        '',
        [Validators.required, Validators.min(0), Validators.max(15)],
      ],
      number_bag: [
        '',
        [Validators.required, Validators.min(0), Validators.max(8)],
      ],
      caution: ['', [Validators.required, Validators.min(0)]],
      clim: [false],
      age_min: [
        '',
        [Validators.required, Validators.min(18), Validators.max(80)],
      ],
    });
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    const files = event.dataTransfer?.files;
    if (files) {
      this.handleFiles(Array.from(files));
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.handleFiles(Array.from(input.files));
    }
  }

  private handleFiles(files: File[]): void {
    const imageFiles = files.filter((file) => file.type.startsWith('image/'));

    imageFiles.forEach((file) => {
      const reader = new FileReader();
      reader.onload = (e: ProgressEvent<FileReader>) => {
        this.selectedFiles.push({
          file,
          preview: e.target?.result as string,
          name: file.name,
        });
      };
      reader.readAsDataURL(file);
    });
  }

  removeFile(index: number): void {
    this.selectedFiles.splice(index, 1);
  }

  onSubmit(): void {
    // Démarrer le chargement
    this.loading = true;
    console.log('boutton cliked');
    // Vérifiez si le formulaire est valide et s'il y a des fichiers sélectionnés
    if (this.carForm.valid && this.selectedFiles.length > 0) {
      const formData = new FormData();
      this.carForm.value.brand = this.carForm.value.brand.toUpperCase();
      this.carForm.value.type = this.carForm.value.type.toUpperCase();
      this.carForm.value.fuel = this.carForm.value.fuel.toUpperCase();

      // Ajoutez les valeurs du formulaire au FormData
      Object.keys(this.carForm.value).forEach((key) => {
        formData.append(key, this.carForm.value[key]);
      });

      // Ajoutez les fichiers sélectionnés
      this.selectedFiles.forEach((fileObj) => {
        formData.append('photoFile', fileObj.file); // Assurez-vous que le backend attend 'photoFile'
      });
      console.log(this.selectedFiles); // Vérifie que ce tableau contient bien les fichiers sélectionnés

      // Appelez le service CarService pour envoyer le FormData à l'API
      this.carService.create(formData).subscribe({
        next: (res) => {
          console.log('Car added successfully');
          this.router.navigate(['/cars']); // Redirige vers la liste des voitures après ajout
          this.formSubmitted.emit(); // Émet l'événement pour notifier le parent, si nécessaire
        },
        error: (err) => {
          console.log('Error', err);
          this.loading = false; // Arrêter le chargement en cas d'erreur
          // Vous pouvez afficher un message d'erreur ici si vous le souhaitez
        },
        complete: () => {
          this.loading = false; // Arrêter le chargement à la fin de la soumission
        },
      });
    } else {
      // Marquer tous les contrôles comme touchés pour afficher les erreurs de validation
      Object.keys(this.carForm.controls).forEach((key) => {
        const control = this.carForm.get(key);
        if (control?.invalid) {
          control.markAsTouched();
        }
      });

      // Arrêter le chargement si le formulaire est invalide
      this.loading = false;
    }
  }
}
