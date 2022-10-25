package fr.iocean.securitydemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/public")
public class PublicController {

    @GetMapping
    public String hello() {
        return "Hello world";
    }
}
