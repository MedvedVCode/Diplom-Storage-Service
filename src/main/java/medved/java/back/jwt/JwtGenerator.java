package medved.java.back.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtGenerator {
    @Value("${jwt.token.expiration}")
    private long jwtExpiration;
    @Value("${jwt.token.secret}")
    private String jwtSecret;

    public String generateToken(Authentication authentication) {
        log.info("-> Generate Token");
        String username = authentication.getName();
        log.info("-> username {}", username);
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpiration);
        log.info("current date {}, expireDate {}", currentDate, expireDate);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
        log.info("-> generate token {}", token);
        return token;
    }

    public String getUsernameFromJWT(String token) {
        log.info("-> Get Username from Token");
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        log.info("-> clails.getSubject {}", claims.getSubject());
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        log.info("-> Check for valid Token");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect!");
        }
    }
}
