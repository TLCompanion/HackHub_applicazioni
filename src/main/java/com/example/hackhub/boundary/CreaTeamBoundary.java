package com.example.hackhub.boundary;

import com.example.hackhub.handler.CreaTeamHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/creaTeam")
public class CreaTeamBoundary {

    private final CreaTeamHandler handler;

    public CreaTeamBoundary(CreaTeamHandler handler) {
        this.handler = handler;
    }

    @PostMapping("")
    public ResponseEntity<Void> avviaCreazioneTeam(
            @AuthenticationPrincipal String nomeUtente,
            @RequestBody String nomeTeam
            ){
        handler.avviaCreazioneTeam(nomeUtente, nomeTeam);
        return ResponseEntity.noContent().build();
    }
}
