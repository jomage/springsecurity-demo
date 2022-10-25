package fr.iocean.securitydemo.repository;

import fr.iocean.securitydemo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Va nous permettre de récupérer un utilisateur par son login.
     */
    User findFirstByLogin(String login);
}
