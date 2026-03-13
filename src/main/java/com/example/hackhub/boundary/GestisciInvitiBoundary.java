package com.example.hackhub.boundary;

import com.example.hackhub.boundary.dto.RichiestaDTO;
import com.example.hackhub.handler.GestisciInvitiHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/inviti")
public class GestisciInvitiBoundary {

    private final GestisciInvitiHandler handler;

    public GestisciInvitiBoundary(GestisciInvitiHandler handler) { this.handler = handler; }

    /**
     * Metodo del boundary che accetta una richiesta di invito Staff, Team o una propostaCall
     * @param idRichiesta l'identificativo della richiesta
     * @return una nuova risposta accettata per lo staff
     */
    @PostMapping("/{idRichiesta}/accetta")
    public ResponseEntity<Void> accettaRichiesta(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String idRichiesta
    ) {
        String idUtente = jwt.getSubject();
        handler.accettaRichiesta(idUtente, idRichiesta);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * Metodo del boundary che rifiuta una richiesta di invito Staff, Team o una proposta di call
     * @param idRichiesta l'identificativo della richiesta
     * @return una nuova risposta rifiutata per lo staff
     */
    @PostMapping("/{idRichiesta}/rifiuta")
    public ResponseEntity<Void> rifiutaRichiesta(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String idRichiesta
    ) {
        handler.rifiutaRichiesta(idRichiesta);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
