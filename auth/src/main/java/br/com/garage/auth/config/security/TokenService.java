package br.com.garage.auth.config.security;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import br.com.garage.auth.models.Usuario;

@Service
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private ServletContext servletContext;

    @Value("${jwt.secret}")
    private String secret;

    public String buildToken(Authentication authenticate) {

        Usuario user = (Usuario) authenticate.getPrincipal();
        var expirationDate = LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));

        String roles = user.getRoles().stream().map(x -> String.valueOf(x.getRoleName())).collect(Collectors.joining(","));

        try {
            return JWT.create()
                    .withIssuer("auth")
                    .withSubject(user.getEmail())
                    .withExpiresAt(expirationDate)
                    .withClaim("role", roles)
                    .withClaim("tenant_id", user.getTenant().getId().toString())
                    .withClaim("user_id", user.getId().toString())
                    .sign(Algorithm.HMAC256(secret));

        } catch (JWTCreationException exception) {
            throw new RuntimeException("ERROR WHILE GENERATING TOKEN", exception);
        }
    }

    public String loadUserInfo(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm).withIssuer("auth").build().verify(token);
            loadUserInfo(jwt);

            return jwt.getSubject();
        } catch (JWTVerificationException exception) {
            log.error(exception.getMessage());
            return null;
        }
    }

    private void loadUserInfo(DecodedJWT jwt) {

        String roles = jwt.getClaim("role").asString();
        String email = jwt.getSubject();
        String tenantId = jwt.getClaim("tenant_id").asString();
        String userId = jwt.getClaim("user_id").asString();

        var userInfo = new UserAuthInfo(tenantId, userId, email, roles);
        servletContext.setAttribute("userInfo", userInfo);
    }

}
