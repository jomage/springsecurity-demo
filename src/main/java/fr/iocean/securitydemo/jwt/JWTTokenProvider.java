package fr.iocean.securitydemo.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Classe utilitaire contenant des méthodes pour obtenir et valider un JWT.
 */
@Component
public class JWTTokenProvider {

    private final Logger log = LoggerFactory.getLogger(JWTTokenProvider.class);

    @Value("${jwt.base64-secret}")
    private String secretKey;

    private Key key;

    private JwtParser jwtParser;

    private final long validityInMilliseconds = 3600000; // 1h

    @PostConstruct
    protected void init() {
        // Encode la secret key en base64 et la transforme en key

        key = new SecretKeySpec(
                Base64.getDecoder().decode(secretKey),
                SignatureAlgorithm.HS256.getJcaName());


        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    /**
     * Fournit un JWT signé, contenant le nom de l'utilisateur et ses droits.
     *
     * @param username le nom de l'utilisateur qui est en train de se logger
     * @param roles les roles qui sont attribués à l'utilisateur
     * @return un String représentant notre JWT.
     */
    public String createToken(String username, Collection<? extends String> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    /**
     * Vérifie la conformité du token JWT
     * @param authToken un String représentant le JWT.
     * @return true si valide, false ou throw exception sinon.
     */
    public boolean validateToken(String authToken) {

        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            log.trace("Invalid JWT token.", e);
        } catch (IllegalArgumentException e) {
            log.error("Token validation error {}", e.getMessage());
        }

        return false;
    }

    /**
     * Récupère les informations de connexion contenus dans le JWT.
     *
     * @param token le JWT
     * @return un objet Authentication (Spring Security) contenant les informations de connexion.
     */
    public Authentication getAuthentication(String token) {
        // Récupère les données du jwt
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        System.out.println(claims);

        // Construit une collection d'Authorities de Spring Security à partir des claims du JWT
        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get("roles").toString().split("[,;\\[\\]]"))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        System.out.println(authorities);

        // Construit un Utilisateur au sens Spring Security, avec les droits
        User principal = new User(claims.getSubject(), "", authorities);

        System.out.println(principal);

        // Retourne un objet Authentication de Spring Security
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
