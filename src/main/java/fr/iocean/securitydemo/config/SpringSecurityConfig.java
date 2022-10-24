package fr.iocean.securitydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    /**
     * Va intercepter les requêtes HTTP et les fait passer à travers une chaîne de filtres
     * pour s'assurer que la ressource demandées est accessible à l'utilisateur.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/rest/admin").hasRole("ADMIN") // si l'url pointe vers /rest/admin, on demande le role "ADMIN" à l'utilisateur.
            .antMatchers("/rest/**").hasRole("USER") // pareil mais pour "USER"
            .anyRequest().authenticated() // pour toutes les autres requêtes, on demande un utilisateur authentifié.
            .and().httpBasic(); // on active l'authentification "Basic Authentication"

        return http.build();
    }

    /**
     * Ce morceau de code ajoute 2 utilisateurs en mémoire (user et admin)
     * Ce sont des utilisateurs au sens "Security" (des infos de connexion et des
     * droits / roles)
     * Cette solution "in memory" est bien sûr non adaptée aux cas réels.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();
        UserDetails userAdmin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin456"))
                .roles("ADMIN", "USER")
                .build();
        return new InMemoryUserDetailsManager(user, userAdmin);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
