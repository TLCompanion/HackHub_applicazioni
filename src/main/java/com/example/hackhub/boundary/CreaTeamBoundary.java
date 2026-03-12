package com.example.hackhub.boundary;

import com.example.hackhub.handler.CreaTeamHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team")
public class CreaTeamBoundary {

    private final CreaTeamHandler handler;

    public CreaTeamBoundary(CreaTeamHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/{nomeTeam}")
    public ResponseEntity<Void> avviaCreazioneTeam(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String nomeTeam
            ){
        String idUtente = jwt.getSubject(); // Ottieni l'ID dell'utente dal token JWT
        handler.avviaCreazioneTeam(idUtente, nomeTeam);
        return ResponseEntity.noContent().build();
    }
}
