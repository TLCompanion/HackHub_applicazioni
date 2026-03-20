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
     * @param nomeMentore il mentore che lo segnala
     * @return una nuova chiamata http
     */
    @PostMapping("/segnalaViolazione")
    public ResponseEntity<Void> segnalaViolazione(
            @AuthenticationPrincipal String nomeOrganizzatore,
            @RequestParam String nomeMentore,
            @RequestParam String nomeTeam
    ){
        handler.segnalaViolazione(nomeOrganizzatore, nomeMentore, nomeTeam);
        return ResponseEntity.ok().build();
    }

    /**
     * Metodo della boundary per nominare mentori
     * @param nomeUtente il nome dell'organizzatore
     * @param nomeUtenteDaInvitare il nome dell'utente da invitare
     * @return una nuova chiamata http
     */
    @PostMapping("/nominaMentori")
    public ResponseEntity<Void> nominaMentori(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nomeUtenteDaInvitare){
        handler.nominaMentori(nomeUtente, nomeUtenteDaInvitare);
        return ResponseEntity.ok().build();
    }

    /**
     * Metodo della boundary per eliminare un hackathon
     * @param nomeUtente l'organizzatore che lo vuole eliminare
     * @param nomeHackathon l'id dell'hackathon
     * @return una nuova chiamata http
     */
    //todo cambiare sul sequence e mettere nomeHackathon e non idHacakthon
    @DeleteMapping("/elimina")
    public ResponseEntity<Void> eliminaHackathon(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nomeHackathon){
        handler.eliminaHackathon(nomeUtente, nomeHackathon);
        return ResponseEntity.ok().build();
    }

    /**
     * Metodo della boundary per espellere un team da un hackathon
     * @param nomeUtente l'organizzatore che espelle il team
     * @param nomeHackathon il nome dell'hackathon
     * @param nomeTeam il nome del team
     * @return una nuova chiamata http
     */
    @PostMapping("/espelleTeam")
    public ResponseEntity<Void> espelliTeam(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nomeHackathon,
            @RequestParam String nomeTeam) {
        handler.espelliTeam(nomeUtente, nomeHackathon, nomeTeam);
        return ResponseEntity.ok().build();
    }

    /**
     * Metodo della boundary per proclamare il vincitore di un hackathon
     * @param nomeUtente l'organizzatore che proclama il vincitore
     * @param nomeHackathon il nome dell'hackathon
     * @param nomeTeam il nome del team
     * @return una nuova chiamata http
     */
    @PostMapping("/proclama")
    public ResponseEntity<Void> proclamaVincitore(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nomeHackathon,
            @RequestParam String nomeTeam
    ){
        handler.proclamaVincitore(nomeUtente, nomeHackathon, nomeTeam);
        return ResponseEntity.ok().build();
    }

    /**
     * Metodo della boundary per liquidare il premio al team vincitore
     * @param nomeUtente l'organizzatore che liquida il premio
     * @param nomeHackathon il nome dell'hackathon
     * @param nomeTeam il nome del team
     * @return una nuova chiamata http
     */
    @PostMapping("/liquidaPremio")
    public ResponseEntity<Void> attivaLiquidazionePremio(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nomeHackathon,
            @RequestParam String nomeTeam){
        handler.attivaLiquidazionePremio(nomeUtente, nomeHackathon, nomeTeam);
        return ResponseEntity.ok().build();
    }
}
