package com.example.hackhub.boundary;

import com.example.hackhub.boundary.dto.NotificaDTO;
import com.example.hackhub.boundary.dto.RichiestaDTO;
import com.example.hackhub.handler.VisualizzaHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/visualizzaListe")
@Validated
public class VisualizzaBoundary {

    private final VisualizzaHandler handler;

    public VisualizzaBoundary(VisualizzaHandler handler) {
        this.handler = handler;
    }

    // TODO creare i dto per gestire le liste di oggetti in questa boundary

    /**
     * Metodo del boundary che ritorna una lista di team
     * @param idHackathon id dell'hackathon di riferimento
     * @param jwt il token jwt dell'utente
     * @return esito della chiamata http
     */
    //TODO falciare questo metodo
    @GetMapping
    public ResponseEntity<Void> viewTeam(String idHackathon, @AuthenticationPrincipal Jwt jwt) {
        handler.viewTeam(idHackathon);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Metodo del boundary che ritorna una lista di valutazioni
     * @param idHackathon id dell'hackathon di riferimento
     * @param jwt il token jwt dell'utente
     * @return esito della chiamata http
     */
    @GetMapping
    public ResponseEntity<Void> viewValutazioni(String idHackathon, @AuthenticationPrincipal Jwt jwt) {
        handler.viewValutazioni(idHackathon);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Metodo del boundary che ritorna una lista di sottomissioni
     * @param idHackathon id dell'hackathon di riferimento
     * @param jwt il token jwt dell'utente
     * @return esito della chiamata http
     */
    @GetMapping
    public ResponseEntity<Void> viewSottomissioni(String idHackathon, @AuthenticationPrincipal Jwt jwt) {
        handler.viewSottomissioni(idHackathon);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Metodo del boundary che ritorna una lista di iscrizioni
     * @param idHackathon id dell'hackathon di riferimento
     * @param jwt il token jwt dell'utente
     * @return esito della chiamata http
     */
    @GetMapping
    public ResponseEntity<Void> viewIscrizioni(String idHackathon, @AuthenticationPrincipal Jwt jwt) {
        handler.viewIscrizioni(idHackathon);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Metodo del boundary che ritorna una lista di richieste
     * @param jwt il token jwt dell'utente
     * @return la lista di dto
     */
    @GetMapping
    public ResponseEntity<List<RichiestaDTO>> viewRichieste(@AuthenticationPrincipal Jwt jwt) {
        String idUtente = jwt.getSubject();
        return ResponseEntity.ok(handler.viewRichieste(idUtente));
    }

    /**
     * Metodo del boundary che ritorna una lista di notifiche
     * @param jwt il token jwt dell'utente
     * @return la lista di dto
     */
    @GetMapping
    public ResponseEntity<List<NotificaDTO>> viewNotifiche(@AuthenticationPrincipal Jwt jwt) {
        String idUtente = jwt.getSubject();
        return ResponseEntity.ok(handler.viewNotifiche(idUtente));
    }
}
