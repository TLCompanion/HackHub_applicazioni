package com.example.hackhub.boundary;

import com.example.hackhub.boundary.dto.HackathonRequest;
import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.handler.AvviaHackathonHandler;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hackathon")
@Validated
public class AvviaHackathonBoundary {

    private final AvviaHackathonHandler handler;

    public AvviaHackathonBoundary(AvviaHackathonHandler handler){
        this.handler = handler;
    }

    @PostMapping("/avvia")
    public ResponseEntity<Void> avviaHackathon(
            @RequestParam Hackathon hackathon){
        handler.avviaHackathon(hackathon);
        return ResponseEntity.noContent().build();
    }
}
