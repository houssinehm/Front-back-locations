package com.example.location_voiture.services;

import com.example.location_voiture.entities.*;
import com.example.location_voiture.exceptions.car.CarNotFoundException;
import com.example.location_voiture.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnonceService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private AgencyRepository agencyRepository;


    private final AnnonceRepository annonceRepository;
    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;


    public AnnonceService(AnnonceRepository annonceRepository, CarRepository carRepository, ReservationRepository reservationRepository) {
        this.annonceRepository = annonceRepository;
        this.carRepository = carRepository;
        this.reservationRepository = reservationRepository;
    }


    public void saveAnnonce(long id_car, double daily_price, boolean cancel_free, String descrption, boolean status) {
        // Obtenir l'email de l'utilisateur connecté
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        // Vérifier que l'utilisateur est une agence
        boolean isAgence = utilisateur.getRole().stream()
                .anyMatch(role -> "ROLE_AGENCE".equalsIgnoreCase(String.valueOf(role.getName())));

        if (!isAgence) {
            throw new RuntimeException("Seules les agences peuvent créer des annonces.");
        }

        // Vérifie si la voiture existe
        Car car = carRepository.findById(id_car)
                .orElseThrow(() -> new CarNotFoundException("Voiture non trouvée"));

        // Créer l'annonce avec les données disponibles
        Annonce annonce = new Annonce();
        annonce.setDescription(descrption);  // Correction de la faute de frappe
        annonce.setStatus(status);
        annonce.setCar(car);
        annonce.setDaily_price(daily_price);  // Ajout du prix journalier
        annonce.setCancel_free(cancel_free);  // Ajout de l'option d'annulation gratuite

        // Sauvegarder l'annonce
        annonceRepository.save(annonce);
    }


    public void supprimerAnnonce(Long id) {
        // Obtenir l'email de l'utilisateur connecté
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        // Vérifier que l'utilisateur est une agence
        boolean isAgence = utilisateur.getRole().stream()
                .anyMatch(role -> "ROLE_AGENCE".equalsIgnoreCase(String.valueOf(role.getName())));

        if (!isAgence) {
            throw new RuntimeException("Seules les agences peuvent supprimer des annonces.");
        }

        // Trouver l'agence associée à l'utilisateur
        Agency agency = agencyRepository.findByUtilisateur(utilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Agence non trouvée"));

        // Trouver l'annonce
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        // Vérifier que l'annonce appartient à l'agence de l'utilisateur
        if (!annonce.getCar().getAgency().equals(agency)) {
            throw new RuntimeException("Vous ne pouvez supprimer que les annonces de votre agence.");
        }

        // Supprimer l'annonce
        annonceRepository.deleteById(id);
    }

    public List<Annonce> getAllAnnonces() {
        // Obtenir l'email de l'utilisateur connecté
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        // Vérifier que l'utilisateur est une agence
        boolean isAgence = utilisateur.getRole().stream()
                .anyMatch(role -> "ROLE_AGENCE".equalsIgnoreCase(String.valueOf(role.getName())));

        if (!isAgence) {
            throw new RuntimeException("Seules les agences peuvent voir leurs annonces.");
        }

        // Trouver l'agence associée à l'utilisateur
        Agency agency = agencyRepository.findByUtilisateur(utilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Agence non trouvée"));

        // Trouver toutes les annonces de l'agence
        List<Annonce> annonces = annonceRepository.findAll().stream()
                .filter(annonce -> annonce.getCar().getAgency().equals(agency))
                .collect(Collectors.toList());

        return annonces;
    }


    public List<Annonce> getAvailableAnnonces(Date dateStart, Date dateEnd, City city) {
        // Vérifie que la ville n'est pas nulle
        if (city == null) {
            throw new IllegalArgumentException("La ville ne peut pas être nulle.");
        }

        // Convertir pour faire les comparaisons
        LocalDate start = dateStart.toLocalDate();
        LocalDate end = dateEnd.toLocalDate();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        // Vérification 1 : début au minimum demain
        if (start.isBefore(tomorrow)) {
            throw new IllegalArgumentException("La date de début doit être au minimum demain.");
        }

        // Vérification 2 : la date de fin doit être après la date de début
        if (end.isBefore(start) || end.isEqual(start)) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début.");
        }

        // Appel au repository
        return annonceRepository.findAvailableAnnoncesFromDatesAndCity(dateStart, dateEnd, city);
    }


    public void updateAnnonce(Long idAnnonce, String description, Double dailyPrice, Boolean status, Boolean cancelFree) {
        // Obtenir l'email de l'utilisateur connecté
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        // Vérifier que l'utilisateur est une agence
        boolean isAgence = utilisateur.getRole().stream()
                .anyMatch(role -> "ROLE_AGENCE".equalsIgnoreCase(String.valueOf(role.getName())));

        if (!isAgence) {
            throw new RuntimeException("Seules les agences peuvent modifier des annonces.");
        }

        // Trouver l'agence associée
        Agency agency = agencyRepository.findByUtilisateur(utilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Agence non trouvée"));

        // Trouver l'annonce
        Annonce annonce = annonceRepository.findById(idAnnonce)
                .orElseThrow(() -> new EntityNotFoundException("Annonce non trouvée"));

        // Vérifier que l'annonce appartient à l'agence
        if (!annonce.getCar().getAgency().equals(agency)) {
            throw new RuntimeException("Vous ne pouvez modifier que les annonces de votre agence.");
        }

        // Mettre à jour les champs non nuls
        if (description != null) {
            annonce.setDescription(description);
        }
        if (dailyPrice != null) {
            annonce.setDaily_price(dailyPrice);
        }
        if (status != null) {
            annonce.setStatus(status);
        }
        if (cancelFree != null) {
            annonce.setCancel_free(cancelFree);
        }

        // Sauvegarder les modifications
        annonceRepository.save(annonce);
    }



}
