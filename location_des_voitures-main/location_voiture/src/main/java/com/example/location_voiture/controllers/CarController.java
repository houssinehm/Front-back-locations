package com.example.location_voiture.controllers;

import com.example.location_voiture.dto.CarUpdateDTO;
import com.example.location_voiture.dto.CarRequest;
import com.example.location_voiture.entities.Car;
import com.example.location_voiture.entities.City;
import com.example.location_voiture.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/agency")
public class CarController {

    @Autowired
    private CarService carService;

    // --- Récupérer toutes les voitures ---
    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    // --- Récupérer une voiture par son ID ---
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        return ResponseEntity.ok(car);
    }


    // --- Créer une nouvelle voiture ---
    @PostMapping("/create")
    public ResponseEntity<Car> createCar(@ModelAttribute CarRequest carRequest) {
        try {
            Car car = carService.createCar(carRequest);
            return new ResponseEntity<>(car, HttpStatus.CREATED);
        } catch (RuntimeException | IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/mes-voitures")
    public ResponseEntity<List<Car>> getMyCars() {
        List<Car> cars = carService.getMyCars();
        return ResponseEntity.ok(cars);}





    // --- Mettre à jour une voiture partiellement ---
    @PutMapping("/update/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @ModelAttribute CarRequest carRequest) {
        try {
            Car updatedCar = carService.updateCar(id, carRequest);
            return new ResponseEntity<>(updatedCar, HttpStatus.OK);
        } catch (RuntimeException | IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // --- Supprimer une voiture ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // --- Récupérer les voitures par ville ---
    @GetMapping("/city/{city}")
    public List<Car> getCarsByCity(@PathVariable City city) {
        return carService.getCarsByCity(city);
    }

    // --- Récupérer les voitures par marque ---
    @GetMapping("/brand/{brand}")
    public List<Car> getCarsByBrand(@PathVariable String brand) {
        return carService.getCarsByBrand(brand);
    }
}
