package com.example.hackhub.boundary;

import com.example.hackhub.controller.CreaTeamHandler;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.domain.implementazione.Utente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
        // TODO in seguito questo sarà tolto, è per testare
        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token mancante o non valido");
        }
        String idUtente = jwt.getSubject(); // Ottieni l'ID dell'utente dal token JWT
        handler.avviaCreazioneTeam(nomeTeam, idUtente);
        return ResponseEntity.status(201).build();
    }

}
