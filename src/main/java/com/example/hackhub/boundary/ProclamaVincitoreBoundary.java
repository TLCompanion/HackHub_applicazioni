package com.example.hackhub.boundary;

import com.example.hackhub.handler.ProclamaVincitoreHandler;
import org.hibernate.boot.internal.Abstract;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vincitori")
public class ProclamaVincitoreBoundary {

    private final ProclamaVincitoreHandler handler;

    public ProclamaVincitoreBoundary(ProclamaVincitoreHandler handler){
        this.handler = handler;
    }

    @PostMapping("/proclama")
    public ResponseEntity<Void> proclamaVincitore(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nomeHackathon,
            @RequestParam String nomeTeam
    ){
        handler.proclamaVincitore(nomeUtente, nomeHackathon, nomeTeam);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/liquidaPremio")
    public ResponseEntity<Void> attivaLiquidazionePremio(
            @AuthenticationPrincipal String nomeUtente,
            @RequestParam String nomeHackathon,
            @RequestParam String nomeTeam){
        handler.attivaLiquidazionePremio(nomeUtente, nomeHackathon, nomeTeam);
        return ResponseEntity.ok().build();
    }
}
