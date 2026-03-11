package com.example.hackhub.boundary;

import com.example.hackhub.handler.GestisceSottomissioneHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sottomissione")
public class GestisceSottomissioneBoundary {

    private final GestisceSottomissioneHandler handler;

    /**
     * Metodo che istanzia la boundary per la gestione delle sottomissioni
     * @param handler
     */
    public GestisceSottomissioneBoundary(GestisceSottomissioneHandler handler) {
        this.handler = handler;
    }

    public ResponseEntity<Void> inviaSottomissione(String idUtente, String link) {
        handler.inviaSottomissione(idUtente, link);
        return ResponseEntity.ok().build();
    }
}
