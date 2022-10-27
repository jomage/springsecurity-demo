package fr.iocean.securitydemo.security;

import fr.iocean.securitydemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionEvaluator {

    @Autowired
    UserRepository userRepository;

    public boolean isIdOkay(Integer id) {
        return id == 1;
    }

    public boolean isUsernameEqualId(String username, Integer id) {
        return userRepository.findFirstByLogin(username).getId().equals(id);
    }
}
