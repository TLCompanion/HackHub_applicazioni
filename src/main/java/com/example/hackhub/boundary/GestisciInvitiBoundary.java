package com.example.hackhub.boundary;

import com.example.hackhub.boundary.dto.InvitiDTO;
import com.example.hackhub.handler.GestisciInvitiHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inviti")
public class GestisciInvitiBoundary {

    private final GestisciInvitiHandler handler;

    public GestisciInvitiBoundary(GestisciInvitiHandler handler) { this.handler = handler; }

    /**
     * Metodo del boundary che ritorna la lista di inviti pendenti di un utente
     * @param idUtente l'identificativo dell'utente
     * @return la lista di inviti
     */
    public List<InvitiDTO> viewInviti(String idUtente) {
        return handler.viewInviti(idUtente);
    }

    /**
     * Metodo del boundary che accetta una richiesta di invito Staff, Team o una propostaCall
     * @param idUtente l'identificativo dell'utente
     * @param idRichiesta l'identificativo della richiesta
     * @return una nuova risposta accettata per lo staff
     */
    public ResponseEntity<Void> accettaRichiesta(String idUtente, String idRichiesta) {
        handler.accettaRichiesta(idUtente, idRichiesta);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * Metodo del boundary che rifiuta una richiesta di invito Staff, Team o una proposta di call
     * @param idRichiesta l'identificativo della richiesta
     * @return una nuova risposta rifiutata per lo staff
     */
    public ResponseEntity<Void> rifiutaInvito(String idRichiesta) {
        handler.rifiutaRichiesta(idRichiesta);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
