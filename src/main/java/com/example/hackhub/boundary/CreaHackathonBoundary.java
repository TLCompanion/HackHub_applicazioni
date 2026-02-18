package com.example.hackhub.boundary;

import com.example.hackhub.boundary.dto.HackathonRequest;
import com.example.hackhub.controller.CreaHackathonHandler;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/hackathon")
public class CreaHackathonBoundary {

    private final CreaHackathonHandler handler;

    public CreaHackathonBoundary(CreaHackathonHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<Void> avviaCreazioneHackathon(
            @NotNull @AuthenticationPrincipal Jwt jwt,
            @NotNull @RequestBody HackathonRequest request
            ) {
        // TODO in seguito questo sarà tolto, è per testare
        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token mancante o non valido");
        }
        String idUtente = jwt.getSubject(); // Ottieni l'ID dell'utente dal token JWT
        handler.avviaCreazioneHackathon(idUtente, request.nome(), request.dataInizio(), request.dataFine(),
                request.luogo(), request.premio(), request.teamMin(), request.teamMax(),  request.maxIscrizioni(),
                request.regolamento(), request.nomeGiudice(), request.nomeMentore());
        return ResponseEntity.status(201).build();
    }

}
