package com.example.hackhub.boundary;

import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.handler.GestisciTeamHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gestisciTeam")
public class GestisciTeamBoundary {

    private final GestisciTeamHandler handler;

    public GestisciTeamBoundary(GestisciTeamHandler handler){
        this.handler = handler;
    }

    @PutMapping("/cambiaNome")
    public ResponseEntity<Void> cambiaNome(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nome,
            @RequestParam Team team
    ){
        handler.cambiaNome(nomeUtente, nome, team);
        //todo ritornare questa response entity è giusto?
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/eliminaMembro")
    public ResponseEntity<Void> esciDalTeam(
            @AuthenticationPrincipal String idMembro,
            @RequestParam String idTeam
    ){
        handler.esciDalTeam(idMembro, idTeam);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/sciogliTeam")
    public ResponseEntity<Void> sciogliTeam(
            @AuthenticationPrincipal String nomeUtente){
        handler.sciogliTeam(nomeUtente);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/espelliMembro")
    public ResponseEntity<Void> espelliMembro(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String idMembro
    ){
        handler.espelliMembro(nomeUtente, idMembro);
        return ResponseEntity.ok().build();
    }
}
