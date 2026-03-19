package com.example.hackhub.boundary;

import com.example.hackhub.handler.GestisceAssistenzaHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistenza")
public class GestisceAssistenzaBoundary {

    private final GestisceAssistenzaHandler handler;

    public GestisceAssistenzaBoundary(GestisceAssistenzaHandler handler){
        this.handler = handler;
    }

    /**
     * Metodo per chiedere assistenza
     * @param nomeUtente il nome utente
     * @param idMentore l'id del mentore
     * @param idHackathon l'id dell'hackathon
     * @return una nuova richiesta di assistenza
     */
    @PostMapping("/richiediAssistenza")
    public ResponseEntity<Void> richiediAssistenza(
        @AuthenticationPrincipal String nomeUtente,
        @RequestParam String idMentore,
        @RequestParam String idHackathon
    ){
        handler.chiediAssistenza(nomeUtente, idMentore, idHackathon);
        return ResponseEntity.ok().build();
    }
}
