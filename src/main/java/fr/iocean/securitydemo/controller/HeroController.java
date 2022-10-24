package fr.iocean.securitydemo.controller;

import fr.iocean.securitydemo.domain.Hero;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/rest/hero")
public class HeroController {

    @GetMapping
    public List<Hero> findAll() {
        Hero hero1 = new Hero();
        hero1.setId(1);
        hero1.setHeroName("Thor");
        hero1.setRealName("Thor");
        hero1.setPower("Lightning hammah");

        Hero hero2 = new Hero();
        hero2.setId(2);
        hero2.setHeroName("The Flash");
        hero2.setRealName("depends");
        hero2.setPower("Goes really fast");

        return Arrays.asList(hero1, hero2);
    }
}
