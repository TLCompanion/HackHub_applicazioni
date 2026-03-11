package com.example.hackhub.boundary;

import com.example.hackhub.handler.CreaTeamHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team")
public class CreaTeamBoundary {

    private final CreaTeamHandler handler;

    public CreaTeamBoundary(CreaTeamHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/creaTeam")
    public ResponseEntity<Void> avviaCreazioneTeam(
            @AuthenticationPrincipal Jwt jwt,
            String nomeTeam
            ){
        String idUtente = jwt.getSubject(); // Ottieni l'ID dell'utente dal token JWT
        handler.avviaCreazioneTeam(nomeTeam, idUtente);
        return ResponseEntity.noContent().build();
    }
}
