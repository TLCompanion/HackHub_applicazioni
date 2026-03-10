package com.example.hackhub.boundary;

import com.example.hackhub.boundary.dto.AuthResponse;
import com.example.hackhub.boundary.dto.LoginRequest;
import com.example.hackhub.boundary.dto.RegisterRequest;
import com.example.hackhub.handler.EffettuaAutenticazioneHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
@Validated
public class EffettuaAutenticazioneBoundary {

    private final EffettuaAutenticazioneHandler handler;

    /**
     * Costruttore che inizializza la boundary
     * @param handler l'handler associato a questo boundary
     */
    public EffettuaAutenticazioneBoundary(EffettuaAutenticazioneHandler handler) {
        this.handler = handler;
    }

    /**
     * Metodo che attiva la procedura di registrazione alla piattaforma
     * @param request il JSON di richiesta di registrazione
     * @return
     */
    public ResponseEntity<Void> attivaRegistrazione(RegisterRequest request) {
        handler.attivaRegistrazione(request); // Avvio la registrazione
        return ResponseEntity.status(HttpStatus.CREATED).build(); // Ritorno il codice di stato created
    }

    /**
     * Metodo che attiva la procedura di login alla piattaforma
     * @param request il JSON di richiesta di login
     * @return
     */
    public ResponseEntity<AuthResponse> attivaAutenticazione(LoginRequest request) {
        return ResponseEntity.ok(handler.attivaAutenticazione(request));
    }
}
