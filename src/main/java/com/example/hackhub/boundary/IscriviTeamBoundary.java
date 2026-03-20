package com.example.hackhub.boundary;

import com.example.hackhub.handler.IscriviTeamHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/hackathon")
public class IscriviTeamBoundary {

    private final IscriviTeamHandler handler;

    public IscriviTeamBoundary(IscriviTeamHandler handler) {
        this.handler = handler;
    }

    /**
     * Metodo della boundary per iscrivere un team ad un hackathon
     * @param nomeUtente il nome utente del leader che vuole iscrivere il team
     * @param nomeHackathon il nome dell'hackathon a cui iscrivere il team
     * @return una nuova chiamata http
     */
    @PostMapping("{nomeHackathon}/iscrizioni")
    public ResponseEntity<Void> avviaIscrizioneHackathon(
            @AuthenticationPrincipal String nomeUtente,
            @PathVariable String nomeHackathon
    ) {
        handler.avviaIscrizioneHackathon(nomeUtente, nomeHackathon);
        return ResponseEntity.noContent().build();
    }

    /**
     * Metodo della boundary per eliminare un iscrizione ad un hackathon
     * @param nomeUtente l'utente che vuole eliminare l'iscrizione
     * @param nomeHackathon l'id dell'hackathon a cui è iscritta il team
     * @return una nuova chiamata http
     */
    @DeleteMapping("{nomeHackathon}/iscrizioni/mia")
    public ResponseEntity<Void> annullaIscrizioneHackathon(
            @AuthenticationPrincipal String nomeUtente,
            @PathVariable String nomeHackathon
    ) {
        handler.annullaIscrizioneHackathon(nomeUtente, nomeHackathon);
        return ResponseEntity.noContent().build();
    }
}
