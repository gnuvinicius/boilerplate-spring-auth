package br.com.garage.auth.config.security;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.auth0.jwt.interfaces.DecodedJWT;
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

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm).withIssuer("auth").build().verify(token).getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    public Map<String, String> validateToken2(String token) {
        Map<String, String> result = new HashMap<>();

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT verify = JWT.require(algorithm).withIssuer("auth").build().verify(token);
            String roles = verify.getClaim("role").asString();
            result.put("role", roles);
            result.put("email", verify.getSubject());
            result.put("tenantId", verify.getClaim("tenant_id").asString());
            result.put("userId", verify.getClaim("user_id").asString());
            return result;
        } catch (JWTVerificationException exception) {
            return result;
        }
    }

}
