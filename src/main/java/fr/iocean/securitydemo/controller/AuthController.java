package fr.iocean.securitydemo.controller;

import fr.iocean.securitydemo.domain.User;
import fr.iocean.securitydemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

/**
 * Contient les méthodes pour gérer l'authentification : log in
 *
 * En Basic Auth, il n'y a pas vraiment besoin d'endpoint spécifique pour s'authentifier, néanmoins le front
 * peut se servir de cet endpoint pour récupérer les infos utilisateurs lors du login.
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Valide la correspondance login/mot-de-passe et renvoie le User correspondant.
     * Les @RequestParams peuvent être remplacés par un body plus conventionnel pour les méthodes POST.
     *
     * @return Basic Auth : renvoie le user authentifié.
     */
    @PostMapping
    public User validateLogin(@RequestParam("login") String login, @RequestParam("password") String password) {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(login, password);

        // Will throw if bad credentials
        authenticationManager.authenticate(authReq);

        return userService.getUserByLogin(login);
    }
}
