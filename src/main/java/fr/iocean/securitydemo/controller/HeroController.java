package fr.iocean.securitydemo.controller;

import fr.iocean.securitydemo.domain.Hero;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


    /*
     * Exemples d'utilisation du @PreAuthorize ci-dessous
     */

    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "Hero administrés";
    }

    @GetMapping("permission")
    @PreAuthorize("@permissionEvaluator.isIdOkay(#id)")
    public String permissionEvaluatorTest(@RequestParam Integer id) {
        return "Bravo, votre id (" + id + ") est OKAY !";
    }

    @GetMapping("johndoe")
    @PreAuthorize("principal.username == 'johndoe'")
    public String johnDoeOnly() {
        return "Bonjour, John Doe !";
    }

    @GetMapping("enabled")
    @PreAuthorize("principal.enabled")
    public String enabledOnly() {
        return "Bonjour, vous êtes activé !";
    }

    @GetMapping("user/{id}")
    @PreAuthorize("@permissionEvaluator.isUsernameEqualId(principal.username, #userId) or hasRole('ADMIN')")
    public String onlySelf(@PathVariable("id") Integer userId) {
        return "vous êtes bien connecté avec l'id " + userId + " (ou vous êtes admin)!";
    }
}
