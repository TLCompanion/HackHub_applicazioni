package com.example.hackhub.boundary;

import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.handler.GestisceHackathonHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gestisciHackathon")
public class GestisceHackathonBoundary {

    private final GestisceHackathonHandler handler;

    public GestisceHackathonBoundary(GestisceHackathonHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/segnalaViolazione")
    public ResponseEntity<Void> segnalaViolazione(
            @AuthenticationPrincipal String idOrganizzatore,
            @RequestParam Team team
    ){
        handler.segnalaViolazione(idOrganizzatore, team);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/nominaMentori")
    public ResponseEntity<Void> nominaMentori(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nomeUtenteDaInvitare){
        handler.nominaMentori(nomeUtente, nomeUtenteDaInvitare);
        return ResponseEntity.ok().build();
    }
}
