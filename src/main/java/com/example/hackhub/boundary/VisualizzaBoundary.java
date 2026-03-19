package com.example.hackhub.boundary;

import com.example.hackhub.boundary.dto.*;
import com.example.hackhub.handler.VisualizzaHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
//TODO controllare tutto uml di questi casi d'uso attentamente
@RestController
@RequestMapping("/api/visualizzaListe")
@Validated
public class VisualizzaBoundary {

    private final VisualizzaHandler handler;

    public VisualizzaBoundary(VisualizzaHandler handler) {
        this.handler = handler;
    }

    /**
     * Metodo del boundary che ritorna una lista di valutazioni
     * @param idHackathon id dell'hackathon di riferimento
     * @return esito della chiamata http
     */
    @GetMapping("/valutazioni")
    public ResponseEntity<List<ValutazioneRequest>> viewValutazioni(
            @RequestParam String idHackathon,
            @AuthenticationPrincipal String nomeUtente) {
        List<ValutazioneRequest> listaValutazioni = handler.viewValutazioni(nomeUtente, idHackathon);
        return ResponseEntity.status(HttpStatus.OK).body(listaValutazioni);
    }

    /**
     * Metodo del boundary che ritorna una lista di sottomissioni
     * @param idHackathon id dell'hackathon di riferimento
     * @return esito della chiamata http
     */
    @GetMapping("/sottomissioni")
    public ResponseEntity<List<SottomissioneDTO>> viewSottomissioni(
            @RequestParam String idHackathon,
            @AuthenticationPrincipal String nomeUtente) {
        List<SottomissioneDTO> listaSottomissioni = handler.viewSottomissioni(nomeUtente, idHackathon);
        return ResponseEntity.status(HttpStatus.OK).body(listaSottomissioni);
    }

    /**
     * Metodo del boundary che ritorna una lista di iscrizioni
     * @param idHackathon id dell'hackathon di riferimento
     * @return esito della chiamata http
     */
    @GetMapping("/iscrizioni")
    public ResponseEntity<List<IscrizioneTeamDTO>> viewIscrizioni(
            @RequestParam String idHackathon,
            @AuthenticationPrincipal String nomeUtente) {
        List<IscrizioneTeamDTO> listaIscrizioni = handler.viewIscrizioni(nomeUtente, idHackathon);
        return ResponseEntity.status(HttpStatus.OK).body(listaIscrizioni);
    }

    /**
     * Metodo del boundary che ritorna una lista di richieste
     * @return la lista di dto
     */
    @GetMapping("/richieste")
    public ResponseEntity<List<RichiestaDTO>> viewRichieste(@AuthenticationPrincipal String nomeUtente) {
        List<RichiestaDTO> listaRichieste = handler.viewRichieste(nomeUtente);
        return ResponseEntity.status(HttpStatus.OK).body(listaRichieste);
    }

    /**
     * Metodo del boundary che ritorna una lista di notifiche
     * @return la lista di dto
     */
    @GetMapping("/notifiche")
    public ResponseEntity<List<NotificaDTO>> viewNotifiche(@AuthenticationPrincipal String nomeUtente) {
        List<NotificaDTO> listaNotifiche = handler.viewNotifiche(nomeUtente);
        return ResponseEntity.status(HttpStatus.OK).body(listaNotifiche);
    }
}
