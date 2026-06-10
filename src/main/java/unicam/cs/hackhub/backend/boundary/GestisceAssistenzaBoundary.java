package unicam.cs.hackhub.backend.boundary;

import unicam.cs.hackhub.backend.handler.GestisceAssistenzaHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistenza")
@CrossOrigin(origins = "http://localhost:5173")
public class GestisceAssistenzaBoundary {

    private final GestisceAssistenzaHandler handler;

    public GestisceAssistenzaBoundary(GestisceAssistenzaHandler handler){
        this.handler = handler;
    }

    /**
     * Metodo per chiedere assistenza
     * @param nomeUtente il nome utente del leader
     * @param nomeMentore l'id del mentore
     * @param nomeHackathon il nome dell'hackathon
     * @return una nuova richiesta di assistenza
     */
    @PostMapping("/richiesta")
    public ResponseEntity<Void> richiediAssistenza(
        @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nomeMentore,
            @RequestParam String nomeHackathon
    ){
        handler.chiediAssistenza(nomeUtente, nomeMentore, nomeHackathon);
        return ResponseEntity.ok().build();
    }
}
