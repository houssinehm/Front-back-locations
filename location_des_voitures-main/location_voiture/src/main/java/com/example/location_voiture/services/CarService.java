package com.example.location_voiture.services;

import com.example.location_voiture.dto.CarUpdateDTO;
import com.example.location_voiture.dto.CarRequest;
import com.example.location_voiture.entities.Agency;
import com.example.location_voiture.entities.Car;
import com.example.location_voiture.entities.City;
import com.example.location_voiture.entities.Utilisateur;
import com.example.location_voiture.repositories.AgencyRepository;
import com.example.location_voiture.repositories.CarRepository;
import com.example.location_voiture.repositories.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private FileStorageService fileStorageService; // <--- AJOUT ICI


    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getMyCars() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        // Vérifier que c'est une agence
        boolean isAgence = utilisateur.getRole().stream()
                .anyMatch(role -> "ROLE_AGENCE".equalsIgnoreCase(String.valueOf(role.getName())));

        if (!isAgence) {
            throw new RuntimeException("Seules les agences peuvent voir leurs voitures");
        }

        Agency agency = agencyRepository.findByUtilisateur(utilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Agence non trouvée"));

        return carRepository.findByAgency(agency);
    }



    public Car getCarById(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        boolean isAgence = utilisateur.getRole().stream()
                .anyMatch(role -> "ROLE_AGENCE".equalsIgnoreCase(String.valueOf(role.getName())));

        if (!isAgence) {
            throw new RuntimeException("Seules les agences peuvent accéder aux voitures");
        }

        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found with id: " + id));

        // Vérifie que la voiture appartient bien à l'agence de l'utilisateur connecté
        Agency agency = agencyRepository.findByUtilisateur(utilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Agence non trouvée"));

        if (!car.getAgency().getId().equals(agency.getId())) {
            throw new RuntimeException("Vous n'avez pas accès à cette voiture");
        }

        return car;
    }

    public Car createCar(CarRequest carRequest) throws IOException {
        // 1. Récupérer l'utilisateur connecté et vérifier son rôle
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Récupération de l'utilisateur avec email : " + email);
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        // Vérification du rôle de l'utilisateur
        boolean isAgence = utilisateur.getRole().stream()
                .anyMatch(role -> "ROLE_AGENCE".equalsIgnoreCase(String.valueOf(role.getName())));


        if (!isAgence) {
            System.out.println("L'utilisateur avec l'email " + email + " n'a pas le rôle requis (ROLE_AGENCE)");
            throw new RuntimeException("Seules les agences peuvent créer des voitures");
        }

        // 2. Récupérer l'agence et créer la voiture
        Agency agency = agencyRepository.findByUtilisateur(utilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Agence non trouvée"));
        System.out.println("Agence récupérée : " + agency);

        // Créer une nouvelle instance de Car à partir des données du CarRequest
        Car car = new Car();
        car.setCity(carRequest.getCity());
        car.setType(carRequest.getType());
        car.setFuel(carRequest.getFuel());
        car.setGearbox(carRequest.getGearbox());
        car.setNumber_place(carRequest.getNumber_place());
        car.setNumber_door(carRequest.getNumber_door());
        car.setClim(carRequest.isClim());
        car.setNumber_bag(carRequest.getNumber_bag());
        car.setCaution(carRequest.getCaution());
        car.setUrl_photo(carRequest.getUrl_photo());
        car.setAge_min(carRequest.getAge_min());
        car.setDriving_license_min_year(carRequest.getDriving_license_min_year());
        car.setModel(carRequest.getModel());
        car.setBrand(carRequest.getBrand());

        MultipartFile photoFile = carRequest.getPhotoFile();  // <--- récupérer la photo depuis CarRequest

        if (photoFile == null || photoFile.isEmpty()) {
            throw new RuntimeException("Une photo est obligatoire pour créer une voiture");
        } else {
            String fileName = fileStorageService.storeFile(photoFile);
            car.setUrl_photo(fileName);
        }


        // Associer l'agence à la voiture
        car.setAgency(agency);

        // Sauvegarder la voiture dans la base de données
        return carRepository.save(car);
    }







    public Car updateCar(Long id, CarRequest carRequest) throws IOException {
        // 1. Récupérer l'utilisateur connecté
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        // 2. Vérifier que l'utilisateur est une agence
        boolean isAgence = utilisateur.getRole().stream()
                .anyMatch(role -> "ROLE_AGENCE".equalsIgnoreCase(String.valueOf(role.getName())));

        if (!isAgence) {
            throw new RuntimeException("Seules les agences peuvent mettre à jour leurs voitures");
        }

        // 3. Récupérer la voiture
        Car car = getCarById(id); // Déjà sécurisé avec contrôle que la voiture appartient à l'agence

        // 4. Mettre à jour les champs
        car.setCity(carRequest.getCity());
        car.setType(carRequest.getType());
        car.setFuel(carRequest.getFuel());
        car.setGearbox(carRequest.getGearbox());
        car.setNumber_place(carRequest.getNumber_place());
        car.setNumber_door(carRequest.getNumber_door());
        car.setClim(carRequest.isClim());
        car.setNumber_bag(carRequest.getNumber_bag());
        car.setCaution(carRequest.getCaution());
        car.setAge_min(carRequest.getAge_min());
        car.setDriving_license_min_year(carRequest.getDriving_license_min_year());
        car.setModel(carRequest.getModel());
        car.setBrand(carRequest.getBrand());

        // 5. Mise à jour de la photo
        MultipartFile newPhoto = carRequest.getPhotoFile();
        if (newPhoto != null && !newPhoto.isEmpty()) {
            String fileName = fileStorageService.storeFile(newPhoto); // Sauvegarder la nouvelle photo
            car.setUrl_photo(fileName); // Mettre à jour l'URL
        } else if (carRequest.getUrl_photo() != null) {
            car.setUrl_photo(carRequest.getUrl_photo()); // Sinon utiliser l'url_photo existante
        }

        // 6. Sauvegarder
        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        // Récupérer l'utilisateur connecté
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        // Vérifier que l'utilisateur est une agence
        boolean isAgence = utilisateur.getRole().stream()
                .anyMatch(role -> "ROLE_AGENCE".equalsIgnoreCase(String.valueOf(role.getName())));

        if (!isAgence) {
            throw new RuntimeException("Seules les agences peuvent supprimer des voitures");
        }

        // Récupérer l'agence associée à l'utilisateur
        Agency agency = agencyRepository.findByUtilisateur(utilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Agence non trouvée"));

        // Récupérer la voiture à supprimer
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Voiture non trouvée avec l'id : " + id));

        // Vérifier que la voiture appartient bien à l'agence connectée
        if (!car.getAgency().getId().equals(agency.getId())) {
            throw new RuntimeException("Vous ne pouvez supprimer que vos propres voitures");
        }

        // Supprimer la voiture
        carRepository.deleteById(id);
    }




    public List<Car> getCarsByCity(City city) {
        return carRepository.findByCity(city);
    }

    public List<Car> getCarsByBrand(String brand) {
        return carRepository.findByBrand(brand);
    }
}
