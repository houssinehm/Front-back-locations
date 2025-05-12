package com.example.location_voiture.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class AnnonceRequest {

    @NotNull
    private String description;

    @NotNull
    private boolean status;

    @NotNull
    private double daily_price;

    @NotNull
    private boolean cancel_free;

    @NotNull
    private long id_car;
}
