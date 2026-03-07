package com.example.hackhub.boundary;

import com.example.hackhub.boundary.dto.PropostaCallRequest;
import com.example.hackhub.controller.GestioneCallHandler;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/call")
public class GestioneCallBoundary {

    private final GestioneCallHandler handler;

    public GestioneCallBoundary(GestioneCallHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/propostaCall")
    public ResponseEntity<Void> avviaPropostaCall(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid PropostaCallRequest request
            ){
            String idUtente = jwt.getSubject(); // Ottieni l'ID dell'utente dal token JWT
              handler.avviaPropostaCall(idUtente, request);
            return ResponseEntity.noContent().build();
    }
}
