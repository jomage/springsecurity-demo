package fr.iocean.securitydemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/admin")
public class AdminController {

    @GetMapping
    public String administrate() {
        return "Trucs administrés avec succès.";
    }
}
