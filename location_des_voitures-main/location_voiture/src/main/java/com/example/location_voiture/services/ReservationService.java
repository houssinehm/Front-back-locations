package com.example.location_voiture.services;

import com.example.location_voiture.entities.Annonce;
import com.example.location_voiture.entities.Client;
import com.example.location_voiture.entities.Reservation;
import com.example.location_voiture.entities.Utilisateur;
import com.example.location_voiture.exceptions.annonce.AnnonceNotFoundException;
import com.example.location_voiture.exceptions.client.ClientNotFoundException;
import com.example.location_voiture.exceptions.reservation.ReservationNotFoundException;
import com.example.location_voiture.repositories.AnnonceRepository;
import com.example.location_voiture.repositories.ClientRepository;
import com.example.location_voiture.repositories.ReservationRepository;
import com.example.location_voiture.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private EmailService emailService;  // Service pour l'envoi d'email

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private final AnnonceRepository annonceRepository;
    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(AnnonceRepository annonceRepository, ClientRepository clientRepository, ReservationRepository reservationRepository) {
        this.annonceRepository = annonceRepository;
        this.clientRepository = clientRepository;
        this.reservationRepository = reservationRepository;
    }

    //  Créer une réservation
    public Reservation saveReservation(long id_annonce, Date date_start, Date date_end) {
        // Récupérer l'utilisateur connecté
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("Utilisateur non trouvé avec l'email : " + email));

        // Vérifier si l'utilisateur est un client
        boolean isClient = utilisateur.getRole().stream()
                .anyMatch(role -> "ROLE_CLIENT".equalsIgnoreCase(role.getName().toString()));
        if (!isClient) {
            throw new RuntimeException("Seul un client peut effectuer une réservation.");
        }

        // Récupérer le client associé à cet utilisateur
        Client client = clientRepository.findByUtilisateur(utilisateur)
                .orElseThrow(() -> new ClientNotFoundException("Client lié à l'utilisateur non trouvé."));

        // Récupérer l'annonce
        Annonce annonce = annonceRepository.findById(id_annonce)
                .orElseThrow(() -> new AnnonceNotFoundException("Annonce non trouvée avec l'ID : " + id_annonce));

        // Vérifier la durée de la réservation
        long daysBetween = ChronoUnit.DAYS.between(date_start.toLocalDate(), date_end.toLocalDate());
        if (daysBetween < 1) {
            throw new RuntimeException("La durée de la réservation doit être d'au moins un jour.");
        }

        // Calcul du prix total
        double price_total = daysBetween * annonce.getDaily_price();

        // Création de la réservation
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setAnnonce(annonce);
        reservation.setDate_start(date_start);
        reservation.setDate_end(date_end);
        reservation.setPrice_total(price_total);

        return reservationRepository.save(reservation);
    }




    // Modifier une réservation existante
    public Reservation updateReservation(Long id, long id_client, long id_annonce, Date date_start, Date date_end, Double price_total) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Réservation non trouvée avec l'ID : " + id));

        Client client = clientRepository.findById(id_client)
                .orElseThrow(() -> new ClientNotFoundException("Client non trouvé avec l'ID : " + id_client));

        Annonce annonce = annonceRepository.findById(id_annonce)
                .orElseThrow(() -> new AnnonceNotFoundException("Annonce non trouvée avec l'ID : " + id_annonce));

        reservation.setClient(client);
        reservation.setAnnonce(annonce);
        reservation.setDate_start(date_start);
        reservation.setDate_end(date_end);
        reservation.setPrice_total(price_total);

        return reservationRepository.save(reservation);
    }

    //  Supprimer une réservation
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Réservation non trouvée avec l'ID : " + id));

        Annonce annonce = reservation.getAnnonce();

        if (!annonce.isCancel_free()) {
            throw new RuntimeException("Annulation non autorisée : l'annonce ne permet pas d'annulation gratuite.");
        }

        // Convertir java.sql.Date (date_start) en LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = reservation.getDate_start().toLocalDate().atStartOfDay();

        long hoursUntilStart = ChronoUnit.HOURS.between(now, startDateTime);
        if (hoursUntilStart < 96) {
            throw new RuntimeException("Annulation non autorisée : il reste moins de 96 heures avant le début de la réservation.");
        }

        reservationRepository.deleteById(id);
    }

    //  Récupérer toutes les réservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}
