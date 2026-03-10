package com.example.hackhub.boundary;

import com.example.hackhub.boundary.dto.ValutazioneRequest;
import com.example.hackhub.handler.ValutazioneHandler;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/*
La tua ValutazioneBoundary deve essere una classe Spring con
@RestController e un endpoint.
 */
@RestController
@Validated
@RequestMapping("/api/sottomissioni")
public class ValutazioneBoundary {
    private final ValutazioneHandler handler;

    public ValutazioneBoundary(ValutazioneHandler handler) {
        this.handler = handler;
    }

    @PostMapping("{id}/valutazione")
    public ResponseEntity<Void> inserisciValutazione(
            @PathVariable("id") String idSottomissione,
            @Valid @RequestBody ValutazioneRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        //TODO 2° iterazione: al posto di idGiudice è meglio mettere idUtenteAutenticato perchè
        // a questo punto non si sa ancora se è effettivamente il giudice, poi controllare che nel
        // jwt si passi l'id perchè se nel jwt c'è l'username e qui si prende l'id sarebbe sbagliato
        String idGiudice = jwt.getSubject(); // Ottieni l'ID del giudice dal token JWT
        handler.avviaInserimentoValutazione(idSottomissione, idGiudice, request);
        return ResponseEntity.noContent().build();
    }
    // Un endpoint di test per verificare che il controller sia raggiungibile
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("ok");
    }
}
