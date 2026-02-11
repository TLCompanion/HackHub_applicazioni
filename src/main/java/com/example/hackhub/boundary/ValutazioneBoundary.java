package com.example.hackhub.boundary;

import com.example.hackhub.boundary.dto.ValutazioneRequest;
import com.example.hackhub.controller.ValutazioneHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/*
La tua ValutazioneBoundary deve essere una classe Spring con
@RestController e un endpoint.
 */
@RestController
@RequestMapping("/api/sottomissioni")
public class ValutazioneBoundary {
    private final ValutazioneHandler handler;

    public ValutazioneBoundary(ValutazioneHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/{id}/valutazione")
    public ResponseEntity<Void> inserisciValutazione(
            @PathVariable("id") String idSottomissione,
            @RequestBody ValutazioneRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        // TODO in seguito questo sarà tolto, è per testare
        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token mancante o non valido");
        }
        String idGiudice = jwt.getSubject(); // Ottieni l'ID del giudice dal token JWT
        handler.avviaInserimentoValutazione(idSottomissione, idGiudice, request.giudizio(), request.punteggio());
        return ResponseEntity.status(201).build();
    }
    // Un endpoint di test per verificare che il controller sia raggiungibile
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("ok");
    }
}
