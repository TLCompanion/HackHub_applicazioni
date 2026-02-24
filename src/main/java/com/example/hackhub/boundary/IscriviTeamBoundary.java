package com.example.hackhub.boundary;

import com.example.hackhub.controller.IscriviTeamHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/iscrizione")
public class IscriviTeamBoundary {

    private final IscriviTeamHandler handler;

    public IscriviTeamBoundary(IscriviTeamHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/iscrizioneTeam")
    public ResponseEntity<Void> avviaIscrizioneHackathon(
            @AuthenticationPrincipal Jwt jwt,
            String nomeHackathon
    ) {
        String idUtente = jwt.getSubject(); // Ottieni l'ID dell'utente dal token JWT
        handler.avviaIscrizioneHackathon(idUtente, nomeHackathon);
        return ResponseEntity.noContent().build();
    }
}
