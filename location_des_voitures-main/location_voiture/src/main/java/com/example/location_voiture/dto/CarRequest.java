package com.example.location_voiture.dto;

import com.example.location_voiture.entities.City;
import com.example.location_voiture.entities.Fuel;
import com.example.location_voiture.entities.Gearbox;
import com.example.location_voiture.entities.Type;
import com.example.location_voiture.entities.Brand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CarRequest {

    @NotNull(message = "City is required")
    private City city;

    @NotNull(message = "Type is required")
    private Type type;

    @NotNull(message = "Fuel is required")
    private Fuel fuel;

    @NotNull(message = "Gearbox is required")
    private Gearbox gearbox;

    @NotNull(message = "Number of places is required")
    @Min(value = 1, message = "Number of places must be at least 1")
    private int number_place;

    @NotNull(message = "Number of doors is required")
    @Min(value = 1, message = "Number of doors must be at least 1")
    private int number_door;

    @NotNull(message = "Climatisation is required")
    private boolean clim;

    @NotNull(message = "Number of bags is required")
    @Min(value = 1, message = "Number of bags must be at least 1")
    private int number_bag;

    @NotNull(message = "Caution is required")
    @Min(value = 0, message = "Caution must be a positive value")
    private float caution;

    @NotBlank(message = "Photo URL is required")
    private String url_photo;

    @NotNull(message = "Minimum age is required")
    @Min(value = 18, message = "Minimum age must be at least 18")
    private int age_min;

    @NotNull(message = "Minimum driving license year is required")
    @Min(value = 0, message = "Minimum driving license year must be a positive value")
    private int driving_license_min_year;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Brand is required")
    private Brand brand;

    @NotNull(message = "Photo is required")
    private MultipartFile photoFile;  // Champ pour la photo
}
