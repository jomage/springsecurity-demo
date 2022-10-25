package fr.iocean.securitydemo.service;

import fr.iocean.securitydemo.domain.Authority;
import fr.iocean.securitydemo.domain.User;
import fr.iocean.securitydemo.exception.UsernameAlreadyExistsException;
import fr.iocean.securitydemo.repository.AuthorityRepository;
import fr.iocean.securitydemo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe service pour gérer les Utilisateurs.
 */
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        AuthorityRepository authorityRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crée et enregistre un user avec le login et mdp donné et role
     * "USER".
     * @param login le login de l'utilisateur
     * @param mdp le mot de passe dont la version encodée sera sauvegardée
     *           en base
     * @return l'utilisateur créé
     */
    public User createUser(String login, String mdp) {
        if (userRepository.findFirstByLogin(login) != null) {
            throw new UsernameAlreadyExistsException();
        }

        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(passwordEncoder.encode(mdp));

        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById("ROLE_USER").ifPresent(authorities::add);
        newUser.setAuthorities(authorities);

        return userRepository.save(newUser);
    }
}
