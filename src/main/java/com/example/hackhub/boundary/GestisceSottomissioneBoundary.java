package com.example.hackhub.boundary;

import com.example.hackhub.handler.GestisceSottomissioneHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/invia")
    public ResponseEntity<Void> inviaSottomissione(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam  String link) {
        handler.inviaSottomissione(nomeUtente, link);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rimozione")
    public ResponseEntity<Void> attivaRimozioneSottomissione(
            @AuthenticationPrincipal String nomeUtente) {
        handler.attivaRimozioneSottomissione(nomeUtente);
        return ResponseEntity.ok().build();
    }
}
