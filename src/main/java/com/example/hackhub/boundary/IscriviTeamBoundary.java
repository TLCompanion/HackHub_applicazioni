package com.example.hackhub.boundary;

import com.example.hackhub.handler.IscriviTeamHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/iscrizioni")
public class IscriviTeamBoundary {

    private final IscriviTeamHandler handler;

    public IscriviTeamBoundary(IscriviTeamHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/team")
    public ResponseEntity<Void> avviaIscrizioneHackathon(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam String nomeHackathon
    ) {
        String idUtente = jwt.getSubject(); // Ottieni l'ID dell'utente dal token JWT
        handler.avviaIscrizioneHackathon(idUtente, nomeHackathon);
        return ResponseEntity.noContent().build();
    }
}
