package net.scero.test.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenSecurity {
    //---- Variables ----//
    private final String secret;

    //---- Constructors ----//
    public TokenSecurity() {
        secret = UUID.randomUUID().toString();
    }

    //---- Public Methods ----//
    public String createJWT(User user, long expiration) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        Claims claims = Jwts.claims().setSubject(user.getUser());
        claims.put("userId", user.getUserId());
        claims.put("role", user.getRole());
        claims.setIssuedAt(Date.from(now.toInstant()));
        claims.setExpiration(Date.from(now.toLocalDateTime().plusSeconds(expiration).toInstant(ZoneOffset.UTC)));

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public User authenticate(String token) throws JwtException {
        Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return new User(body.getSubject(), Integer.parseInt(body.get("userId").toString()), body.get("role").toString());
    }

    //---- Private Methods ----//
}
