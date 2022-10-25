package fr.iocean.securitydemo.controller;

import fr.iocean.securitydemo.domain.User;
import fr.iocean.securitydemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public User createUser(
            @RequestParam("login") String login,
            @RequestParam("mdp") String mdp) {
        return userService.createUser(login, mdp);
    }
}
