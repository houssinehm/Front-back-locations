package com.example.location_voiture.repositories;

import com.example.location_voiture.entities.Annonce;
import com.example.location_voiture.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface AnnonceRepository extends JpaRepository<Annonce, Long> {
    List<Annonce> findAll();
    @Query("SELECT a FROM Annonce a " +
            "JOIN a.car c " + // Jointure avec Car
            "WHERE (NOT EXISTS (SELECT r FROM Reservation r WHERE r.annonce = a AND " +
            "(r.date_start < :endDate AND r.date_end > :startDate))) " + // Exclure les réservations qui se chevauchent
            "AND c.city = :city " + // Filtrer par la ville de la voiture (Car)
            "AND a.status = true") // Ajouter la condition pour exclure les annonces inactives
    List<Annonce> findAvailableAnnoncesFromDatesAndCity(@Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate,
                                                        @Param("city") City city);

}