package com.example.location_voiture.repositories;

import com.example.location_voiture.entities.Agency;
import com.example.location_voiture.entities.Car;
import com.example.location_voiture.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car,Long>  {

    List<Car> findByCity(City city);
    List<Car> findByBrand(String brand);
    List<Car> findByFuel(String fuel);
    List<Car> findByAgencyId(Long agencyId);
    List<Car> findByAgency(Agency agency);


}
