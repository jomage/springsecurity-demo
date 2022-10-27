package fr.iocean.securitydemo.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filtre que l'on va insérer dans la FilterChain de Spring Security afin de valider le token fourni.
 */
public class JWTFilter extends GenericFilterBean {

    private final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JWTTokenProvider jwtTokenProvider;

    public JWTFilter(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);

        logger.debug("]]]]]]]]]]]]]]]]]]");
        logger.debug("token {}",  jwt);
        logger.debug("hasText {}", StringUtils.hasText(jwt));
        logger.debug("validate {}", this.jwtTokenProvider.validateToken(jwt));

        if (StringUtils.hasText(jwt) && this.jwtTokenProvider.validateToken(jwt)) {
            Authentication authentication = this.jwtTokenProvider.getAuthentication(jwt);

            logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            logger.debug("isAuth {}", authentication.isAuthenticated());
            logger.debug("name {}", authentication.getName());
            logger.debug("cred {}", authentication.getCredentials());
            logger.debug("det {}", authentication.getDetails());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
