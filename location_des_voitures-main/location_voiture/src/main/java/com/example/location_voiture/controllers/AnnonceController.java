package com.example.location_voiture.controllers;

import com.example.location_voiture.dto.AnnonceRequest;
import com.example.location_voiture.entities.Annonce;
import com.example.location_voiture.entities.City;
import com.example.location_voiture.services.AnnonceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/annonces")
public class AnnonceController {

    @Autowired
    private AnnonceService annonceService;

    // Récupérer toutes les annonces
    @GetMapping
    public ResponseEntity<List<Annonce>> getAllAnnonces() {
        List<Annonce> annonces = annonceService.getAllAnnonces();
        return ResponseEntity.ok(annonces);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createAnnonce(@RequestBody AnnonceRequest request) {
        try {
            // Appel à ton service pour sauvegarder l'annonce
            annonceService.saveAnnonce(
                    request.getId_car(),
                    request.getDaily_price(),
                    request.isCancel_free(),
                    request.getDescription(),
                    request.isStatus()
            );

            // Réponse de succès sous forme d'un objet JSON
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");  // Indication de succès
            response.put("message", "Annonce créée avec succès.");

            return ResponseEntity.ok(response); // Retourne la réponse avec un code HTTP 200 (OK)
        } catch (Exception e) {
            // En cas d'erreur, retourne un objet JSON avec l'erreur
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Erreur : " + e.getMessage());

            return ResponseEntity.badRequest().body(response); // Retourne un code HTTP 400 (Bad Request)
        }
    }

    // Supprimer une annonce par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        try {
            annonceService.supprimerAnnonce(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAnnonce(
            @PathVariable Long id,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double daily_price,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Boolean cancel_free
    ) {
        try {
            annonceService.updateAnnonce(id, description, daily_price, status, cancel_free);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Annonce>> getAvailableAnnonces(
            @RequestParam("start") String startStr,
            @RequestParam("end")   String endStr,
            @RequestParam("city")  String cityStr) {

        // 1. Parser les dates en LocalDate
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(startStr);  // ISO yyyy-MM-dd
            endDate   = LocalDate.parse(endStr);
        } catch (DateTimeParseException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);  // Format de date invalide
        }

        // 2. Convertir en java.sql.Date
        Date sqlStart = Date.valueOf(startDate);
        Date sqlEnd   = Date.valueOf(endDate);

        // 3. Valider et convertir la ville
        City city;
        try {
            city = City.valueOf(cityStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);  // Ville invalide
        }

        // 4. Appel du service avec rattrapage des erreurs métier
        try {
            List<Annonce> result = annonceService.getAvailableAnnonces(sqlStart, sqlEnd, city);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ex) {
            // Ici on retourne 400 et on peut renvoyer le message pour débogage
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }


    @GetMapping("/mes-annonces")
    public ResponseEntity<List<Annonce>> getAnnoncesByAgence() {
        try {
            List<Annonce> annonces = annonceService.getAllAnnonces();
            return ResponseEntity.ok(annonces);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}




