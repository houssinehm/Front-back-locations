package com.example.location_voiture.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterRequest {
    @Email
    @NotBlank
    private String email;
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;
    private Set<String> roles;
    @NotNull(message = "Le numéro de téléphone ne peut pas être nul")
    private String telephone;

    // Champs spécifiques à Client
    private String first_name;
    private String name;
    private Integer age;
    // Champs spécifiques à Agence
    private String nom_agence;
    private int nombre_voiture;
    private String adresse;

}
