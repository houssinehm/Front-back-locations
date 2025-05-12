package com.example.location_voiture.dto;

import com.example.location_voiture.entities.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarUpdateDTO {
    private City city;
    private Double price;
    private Type type;
    private Fuel fuel;
    private Gearbox gearbox;
    private Integer number_place;
    private Integer number_door;
    private Boolean clim;
    private Integer number_bag;
    private Float caution;
    private String url_photo;
    private Integer age_min;
    private Integer driving_license_min_year;
    private String model;
    private Brand brand;
    private Long agencyId;
}
