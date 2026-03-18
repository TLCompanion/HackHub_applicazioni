package com.example.hackhub.boundary;

import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.handler.GestisceHackathonHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gestisciHackathon")
public class GestisceHackathonBoundary {

    private final GestisceHackathonHandler handler;

    public GestisceHackathonBoundary(GestisceHackathonHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<Void> segnalaViolazione(
            @AuthenticationPrincipal String idOrganizzatore,
            @RequestParam Team team
    ){
        handler.segnalaViolazione(idOrganizzatore, team);
        return ResponseEntity.ok().build();
    }
}
