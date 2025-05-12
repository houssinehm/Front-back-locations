package com.example.location_voiture.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "annonce")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Annonce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double daily_price;

    @Column(nullable = false)
    private boolean cancel_free;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean status;

    @OneToOne
    @JoinColumn(name = "id_car", referencedColumnName = "id")
    private Car car;

}