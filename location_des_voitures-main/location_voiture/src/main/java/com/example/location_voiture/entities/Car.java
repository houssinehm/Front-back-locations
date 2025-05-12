package com.example.location_voiture.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "car")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private City city;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Fuel fuel;

    @Enumerated(EnumType.STRING)
    private Gearbox gearbox;

    @Column(nullable = false)
    private int number_place;

    @Column(nullable = false)
    private int number_door;

    @Column(nullable = false)
    private boolean clim;

    @Column(nullable = false)
    private int number_bag;

    @Column(nullable = false)
    private float caution;

    @Column(nullable = false)
    private String url_photo;

    @Column(nullable = false)
    private int age_min;

    @Column(nullable = false)
    private int driving_license_min_year;

    @Column(nullable = false)
    private String model;

    @Enumerated(EnumType.STRING)
    private Brand brand;


    @ManyToOne
    @JoinColumn(name = "agency_id", referencedColumnName = "id")
    private Agency agency;

}