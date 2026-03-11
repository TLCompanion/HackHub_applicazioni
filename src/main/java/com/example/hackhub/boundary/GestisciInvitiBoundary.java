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
     * @param idUtente
     * @return
     */
    public List<InvitiDTO> viewInviti(String idUtente) {
        return handler.viewInviti(idUtente);
    }

    /**
     * Metodo del boundary che accetta una richiesta di invito Staff
     * @param idUtente
     * @param idRichiesta
     * @return
     */
    public ResponseEntity<Void> accettaInvitoStaff(String idUtente, String idRichiesta) {
        handler.accettaInvitoStaff(idUtente, idRichiesta);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * Metodo del boundary che accetta una richiesta di invito Team
     * @param idUtente
     * @param idRichiesta
     * @return
     */
    public ResponseEntity<Void> accettaInvitoTeam(String idUtente, String idRichiesta) {
        handler.accettaInvitoTeam(idUtente, idRichiesta);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * Metodo del boundary che rifiuta una richiesta di invito Staff
     * @param idUtente
     * @param idRichiesta
     * @return
     */
    public ResponseEntity<Void> rifiutaInvitoStaff(String idUtente, String idRichiesta) {
        handler.accettaInvitoStaff(idUtente, idRichiesta);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Metodo del boundary che rifiuta una richiesta di invito Team
     * @param idUtente
     * @param idRichiesta
     * @return
     */
    public ResponseEntity<Void> rifiutaInvitoTeam(String idUtente, String idRichiesta) {
        handler.accettaInvitoTeam(idUtente, idRichiesta);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
