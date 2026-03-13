package com.example.hackhub.boundary;

import com.example.hackhub.handler.RispondeRichiesteSupportoHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/richieste_supporto")
@Validated
public class RispondeRichiesteSupportoBoundary {

    private final RispondeRichiesteSupportoHandler handler;

    public RispondeRichiesteSupportoBoundary(RispondeRichiesteSupportoHandler handler) {
        this.handler = handler;
    }

    /**
     * Metodo che ritorna l'esito della chiamata http, e che risponde a una richiesta di supporto con
     * una notifica
     * @param idNotifica l'id della notifica che richiede supporto a un mentore di un hackathon
     * @return l'esito della chiamata http, e salva nel db una proposta call oppure una notifica di risposta
     */
    @PutMapping()
    public ResponseEntity<Void> rispondiRichiestaSupportoConNotifica(String idNotifica, Jwt jwt) {
        String idMentore = jwt.getSubject();
        handler.rispondiRichiestaSupportoConNotifica(idMentore, idNotifica);
        return ResponseEntity.ok().build();
    }
}
