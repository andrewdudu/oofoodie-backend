package com.oofoodie.backend.handler;

import com.oofoodie.backend.models.entity.Role;
import com.oofoodie.backend.models.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static com.oofoodie.backend.constants.Security.AUTHORITIES_KEY;

@Component
public class TokenProvider implements Serializable {

    @Value("${authentication.accessTokenExpirationInMs}")
    private Long ACCESS_TOKEN_EXPIRATION_IN_MS;

    @Value("${authentication.refreshTokenExpirationInMs}")
    private Long REFRESH_TOKEN_EXPIRATION_IN_MS;

    @Value("3600000")
    private Long FORGOT_PASSWORD_TOKEN;

    @Value("${authentication.tokenSecret}")
    private String TOKEN_SECRET;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(TOKEN_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        final List<Role> authorities = new ArrayList<>(user.getRoles());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_IN_MS))
                .compact();
    }

    public String generateRefreshToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_IN_MS))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();
    }

    public String generatePasswordResetToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + FORGOT_PASSWORD_TOKEN))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(TOKEN_SECRET).parse(token);
            return true;
        } catch (SignatureException ex) {
            printLog(ex);
        } catch (MalformedJwtException ex) {
            printLog(ex);
        } catch (ExpiredJwtException ex) {
            printLog(ex);
        } catch (UnsupportedJwtException ex) {
            printLog(ex);
        } catch (IllegalArgumentException ex) {
            printLog(ex);
        }
        return false;
    }

    private void printLog(Exception ex) {
        Logger logger = LoggerFactory.getLogger(ex.getClass());
        logger.error(ex.getMessage());
    }

}
