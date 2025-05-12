package com.example.location_voiture.controllers;

import com.example.location_voiture.dto.ReservationRequest;
import com.example.location_voiture.entities.Reservation;
import com.example.location_voiture.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // Récupérer toutes les Reservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    // Créer une nouvelle reservation
    @PostMapping("/create")
    public ResponseEntity<Void> createReservation(@RequestBody ReservationRequest request) {
        try {
            // Utiliser les données de request pour créer la réservation
            reservationService.saveReservation(
                    request.getAnnonceId(),
                    request.getDateStart(),
                    request.getDateEnd()
            );
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Supprimer une reservation par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}