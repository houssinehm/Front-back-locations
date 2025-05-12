import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { CarService } from '../../../../core/car.service';
import { Car } from '../../../../models/car';

@Component({
  selector: 'app-edit-car',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-car.component.html',
})
export class EditCarComponent implements OnChanges {
  @Input() car!: Car;
  @Output() formSubmitted = new EventEmitter<void>();
  @Output() cancelEdit = new EventEmitter<void>();
  carForm: FormGroup;
  selectedFiles: File[] = [];
  loading = false;

  constructor(private fb: FormBuilder, private carService: CarService) {
    this.carForm = this.fb.group({
      city: ['', Validators.required],
      brand: ['', Validators.required],
      model: ['', Validators.required],
      type: ['', Validators.required],
      fuel: ['', Validators.required],
      gearbox: ['', Validators.required],
      number_place: ['', [Validators.required, Validators.min(1), Validators.max(9)]],
      number_door: ['', [Validators.required, Validators.min(2), Validators.max(5)]],
      driving_license_min_year: ['', [Validators.required, Validators.min(18), Validators.max(25)]],
      number_bag: ['', [Validators.required, Validators.min(1), Validators.max(5)]],
      caution: ['', [Validators.required, Validators.min(0)]],
      clim: [null, Validators.required],
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['car'] && this.car) {
      this.carForm.patchValue(this.car);
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.selectedFiles = Array.from(input.files);
    }
  }

  onSubmit(): void {
    if (this.carForm.valid) {
      this.loading = true;
      const updatedCar = { ...this.car, ...this.carForm.value };
      if (this.selectedFiles.length > 0) {
        updatedCar.photoFile = this.selectedFiles[0];
      }
      this.carService.update(this.car.id, updatedCar).subscribe({
        next: () => {
          this.formSubmitted.emit();
        },
        error: () => {
          this.loading = false;
        },
        complete: () => {
          this.loading = false;
        },
      });
    } else {
      Object.values(this.carForm.controls).forEach(control => control.markAsTouched());
    }
  }
} 