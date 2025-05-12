package com.example.location_voiture.repositories;

import com.example.location_voiture.entities.Annonce;
import com.example.location_voiture.entities.Client;
import com.example.location_voiture.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findAll();

}
