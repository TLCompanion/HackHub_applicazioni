package unicam.cs.hackhub.backend.boundary;

import unicam.cs.hackhub.backend.handler.InvitaUtentiHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team")
@CrossOrigin(origins = "http://localhost:5173")
public class InvitaUtentiBoundary {

    private final InvitaUtentiHandler handler;

    public InvitaUtentiBoundary(InvitaUtentiHandler handler){
        this.handler = handler;
    }

    /**
     * Metodo della boundary che invita un utente ad un team
     * @param nomeUtente il nome dell'utente che invita
     * @param nomeUtenteDaInvitare l'utente da invitare
     * @return una nuova risposta http
     */
    @PostMapping("/mio/invito")
    public ResponseEntity<Void> InvitaUtenti(@AuthenticationPrincipal String nomeUtente,
                                             @RequestParam String nomeUtenteDaInvitare){
        handler.invitaUtenti(nomeUtente, nomeUtenteDaInvitare);
        return ResponseEntity.noContent().build();
    }
}
