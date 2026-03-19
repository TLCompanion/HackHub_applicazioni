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

    /**
     * Metodo della boundary per segnalare una violazione
     * @param nomeOrganizzatore il nome dell'organizzatore
     * @param team il team che ha violato le regole
     * @return una nuova chiamata http
     */
    @PostMapping("/segnalaViolazione")
    public ResponseEntity<Void> segnalaViolazione(
            @AuthenticationPrincipal String nomeOrganizzatore,
            @RequestParam Team team
    ){
        handler.segnalaViolazione(nomeOrganizzatore, team);
        return ResponseEntity.ok().build();
    }

    /**
     * Metodo della boundary per nominare mentori
     * @param nomeUtente il nome dell'organizzatore
     * @param nomeUtenteDaInvitare il nome dell'utente da invitare
     * @return una nuova chiamata http
     */
    @PutMapping("/nominaMentori")
    public ResponseEntity<Void> nominaMentori(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nomeUtenteDaInvitare){
        handler.nominaMentori(nomeUtente, nomeUtenteDaInvitare);
        return ResponseEntity.ok().build();
    }
}
