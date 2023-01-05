package fr.iocean.securitydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Va intercepter les requêtes HTTP et les fait passer à travers une chaîne de filtres
     * pour s'assurer que la ressource demandées est accessible à l'utilisateur.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable() // obligé de désactiver la protection csrf pour tester en local avec postman.
                .authorizeRequests((authorize) -> authorize
                                // si l'url pointe vers /rest/admin ou /rest/user, on demande le role "ADMIN" à l'utilisateur.
                                .antMatchers("/rest/admin/**").hasRole("ADMIN")
                                .antMatchers("/rest/user/**").hasRole("ADMIN")

                                // pour /rest/hero on demande "ADMIN" ou "USER"
                                .antMatchers("/rest/hero/**").hasAnyRole("ADMIN", "USER")

                                .antMatchers("/auth").permitAll()

                                // /rest/public est accessible à tout le monde.
                                .antMatchers("/rest/public/**").permitAll()

                        // on pourrait par ex. demander une authentification pour toutes les autres requêtes :
                        //.anyRequest().authenticated()
                )
                .httpBasic(); // on active l'authentification "Basic Authentication"

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // si on veut totalement outrepasser l'encodage :
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }
}
