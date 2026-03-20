package com.example.hackhub.boundary;

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

    /**
     * Metodo della boundary per cambiare nome ad un team
     * @param nomeUtente il nome dell'utente che vuole cambiare il nome
     * @param nome il nuovo nome del team
     * @return una nuova chiamata http
     */
    @PutMapping("/cambiaNome")
    public ResponseEntity<Void> cambiaNome(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nome
    ){
        handler.cambiaNomeTeam(nomeUtente, nome);
        return ResponseEntity.ok().build();
    }

    /**
     * Metodo della boundary per uscire da un team
     * @param nomeUtente il nome dell'utente che vuole uscire dal team
     * @param nomeTeam l'id del team
     * @return una nuova chiamata http
     */
    @DeleteMapping("/eliminaMembro")
    public ResponseEntity<Void> esciDalTeam(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nomeTeam
    ){
        handler.esciDalTeam(nomeUtente, nomeTeam);
        return ResponseEntity.ok().build();
    }

    /**
     * Metodo della boundary per sciogliere un team
     * @param nomeUtente il nome dell'utente che vuole sciogliere il team
     * @return una nuova chiamata http
     */
    @DeleteMapping("/sciogliTeam")
    public ResponseEntity<Void> sciogliTeam(
            @AuthenticationPrincipal String nomeUtente){
        handler.sciogliTeam(nomeUtente);
        return ResponseEntity.ok().build();
    }

    /**
     * Metodo della boundary per espellere un membro da un team
     * @param nomeUtente il nome dell'utente che vuole espellere il membro
     * @param idMembro l'id del membro da espellere
     * @return una nuova chiamata http
     */
    @DeleteMapping("/espelliMembro")
    public ResponseEntity<Void> espelliMembro(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String idMembro
    ){
        handler.espelliMembro(nomeUtente, idMembro);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/trasferisciRuolo")
    public ResponseEntity<Void> trasferisceRuoloLeader(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nomeMembro){
        handler.trasferisceRuoloLeader(nomeUtente, nomeMembro);
        return ResponseEntity.ok().build();
    }
}
