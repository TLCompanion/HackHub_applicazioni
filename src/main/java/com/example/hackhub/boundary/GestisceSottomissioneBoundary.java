package com.example.hackhub.boundary;

import com.example.hackhub.handler.GestisceSottomissioneHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sottomissione")
public class GestisceSottomissioneBoundary {

    private final GestisceSottomissioneHandler handler;

    /**
     * Metodo che istanzia la boundary per la gestione delle sottomissioni
     * @param handler l'handler
     */
    public GestisceSottomissioneBoundary(GestisceSottomissioneHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<Void> inviaSottomissione(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam  String link) {
        String idUtente = jwt.getSubject();
        handler.inviaSottomissione(idUtente, link);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> attivaRimozioneSottomissione(Jwt jwt) {
        String idUtente = jwt.getSubject();
        handler.attivaRimozioneSottomissione(idUtente);
        return ResponseEntity.ok().build();
    }
}
