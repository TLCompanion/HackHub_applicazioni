package com.example.hackhub.boundary;

import com.example.hackhub.boundary.dto.HackathonRequest;
import com.example.hackhub.handler.CreaHackathonHandler;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/hackathon")
@Validated
public class CreaHackathonBoundary {

    private final CreaHackathonHandler handler;

    public CreaHackathonBoundary(CreaHackathonHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<Void> avviaCreazioneHackathon(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody HackathonRequest request
            ) {
        String idUtente = jwt.getSubject(); // Ottieni l'ID dell'utente dal token JWT
        handler.avviaCreazioneHackathon(request, idUtente);
        return ResponseEntity.noContent().build();
    }

}
