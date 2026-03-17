package com.example.hackhub.boundary;

import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.handler.InvitaUtentiHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inviti")
public class InvitaUtentiBoundary {

    private final InvitaUtentiHandler handler;

    public InvitaUtentiBoundary(InvitaUtentiHandler handler){
        this.handler = handler;
    }

    @PostMapping("/utenti")
    public ResponseEntity<Void> InvitaUtenti(@AuthenticationPrincipal String nomeUtente,
                                             @RequestParam Team team){
        handler.invitaUtenti(nomeUtente, team);
        return ResponseEntity.noContent().build();
    }
}
