package fr.iocean.securitydemo.config;

import fr.iocean.securitydemo.jwt.JWTConfigurer;
import fr.iocean.securitydemo.jwt.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    /**
     * Va intercepter les requêtes HTTP et les fait passer à travers une chaîne de filtres
     * pour s'assurer que la ressource demandées est accessible à l'utilisateur.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//            .cors().and()
//            .cors().disable()
            .csrf().disable() // obligé de désactiver la protection csrf pour tester en local avec postman.
            .authorizeRequests((authorize) -> authorize
                // si l'url pointe vers /rest/admin ou /rest/user, on demande le role "ADMIN" à l'utilisateur.
                .antMatchers("/rest/admin/**").hasRole("ADMIN")
                .antMatchers("/rest/user/**").hasRole("ADMIN")

                // pour /rest/hero on demande "ADMIN" ou "USER"
                .antMatchers("/rest/hero/**").hasAnyRole("ADMIN", "USER")

                // /rest/public est accessible à tout le monde.
                .antMatchers("/rest/public/**").permitAll()

                // on pourrait par ex. demander une authentification pour toutes les autres requêtes :
                //.anyRequest().authenticated()
            )
            .httpBasic() // on active l'authentification "Basic Authentication"
            .and()
                .apply(securityConfigurerAdapter()) // on applique le filtre qui check le JWT
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // si on veut totalement outrepasser l'encodage :
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(jwtTokenProvider);
    }
}
