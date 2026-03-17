package com.example.hackhub.servizi;

import com.example.hackhub.domain.implementazione.Utente;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Pattern: Singleton, gestione dei token Jwt
 */
@Service
public class ServizioJwt {

    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    /**
     * Metodo helper per prelevare la chiave
     * @return
     */
    private Key getSigningKey() { return Keys.hmacShaKeyFor(jwtSecret.getBytes()); }

    /**
     * Metodo che genera un token Jwt per un Utente che tenta di accedere alla piattaforma
     * @param utente l'oggetto Utente che prova ad autenticarsi alla piattaforma
     * @return un token Jwt
     */
    public String generaToken(Utente utente) {
        if (utente == null) throw new IllegalArgumentException("L'utente passato è nullo");

        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+jwtExpirationMs);

        return Jwts.builder()
                .setSubject(utente.getNomeUtente())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Metodo per estrarre il nomeUtente da un token Jwt
     * @param token il token Jwt da cui si deve effettuare l'estrazione
     * @return nomeUtente
     */
    public String estraiNomeUtente(String token) {
        if (token == null) throw new IllegalArgumentException("L'utente passato è nullo");

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Verifica che un token Jwt sia valido per un certo Utente
     * @param token il token Jwt da validare
     * @param utente l'utente che prova l'autenticazione alla piattaforma
     * @throws RuntimeException se viene lanciata un'eccezione
     */
    public void validaToken(String token, Utente utente) {
        if (token == null || utente == null)
            throw new IllegalArgumentException("Token o utente passati nulli");

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            if (!claims.getSubject().equals(utente.getNomeUtente()))
                throw new JwtException("Token non corrispondente all'utente");
        }
        catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Token Jwt non valido o scaduto");
        }
    }
}
