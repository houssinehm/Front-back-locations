package com.example.location_voiture.repositories;

import com.example.location_voiture.entities.Agency;
import com.example.location_voiture.entities.Client;
import com.example.location_voiture.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findByUtilisateur(Utilisateur utilisateur);
}
