package com.example.location_voiture.config;


import com.example.location_voiture.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;

// Indique que cette classe est une classe de configuration Spring.
// Elle remplace les fichiers XML de configuration.
@Configuration

// Génère automatiquement un constructeur avec les attributs finaux (final)
// Cela permet d'injecter `jwtAuthenticationFilter` et `userDetailsService` via le constructeur.
@RequiredArgsConstructor
public class SecurityConfig {

    // Filtre personnalisé pour extraire et valider le JWT à chaque requête
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Service utilisé pour charger les détails de l'utilisateur à partir de la base de données
    private final UserDetailsService userDetailsService;

    // Bean pour encoder les mots de passe (ici avec BCrypt)
    // Utilisé pour la comparaison lors de l'authentification
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean qui fournit un AuthenticationManager avec un DaoAuthenticationProvider
    // Ce provider utilise notre UserDetailsService et PasswordEncoder pour l’authentification
    @Bean
    public AuthenticationManager authenticationManager() {
        // Fournisseur d'authentification basé sur les données de la base (DAO)
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Injecte le UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder());     // Définit l’encodeur de mot de passe

        // Retourne un AuthenticationManager basé sur ce provider
        return new ProviderManager(authProvider);
    }

    // Configuration principale de la sécurité HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Désactive la protection CSRF (utile pour les APIs REST où le token est déjà sécurisé)
                .csrf(csrf -> csrf.disable())

                // Configuration des règles d’autorisation selon les routes
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // ✅ Permet les requêtes preflight
                        // Autorise librement l’accès aux routes d’authentification (login, register, etc.)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/cars/**").hasAuthority("ROLE_AGENCE")

                        // Public : recherche d'annonces
                        .requestMatchers("/annonces/disponibles").permitAll()
                        // Ajout de cette ligne pour permettre l'accès aux fichiers dans uploads
                        .requestMatchers("/uploads/**").permitAll()

                        .requestMatchers("/reservations/create").hasAuthority("ROLE_CLIENT")
                        // Protégé : création / suppression / modification ⇒ agence uniquement
                        .requestMatchers("/annonces/create", "/annonces/delete/**", "/annonces/update/**", "/annonces/all").hasAuthority("ROLE_AGENCE")



                        // Restreint l’accès aux routes d’administration aux utilisateurs avec le rôle ADMIN
                        .requestMatchers("/agency/**").hasAuthority("ROLE_AGENCE")

                        // Toutes les autres routes nécessitent une authentification
                        .anyRequest().authenticated()
                )

                // Ajoute notre filtre JWT AVANT le filtre UsernamePasswordAuthenticationFilter
                // Cela permet d’authentifier via JWT au lieu de formulaire classique
               .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Construit la chaîne de filtres de sécurité
                .build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
