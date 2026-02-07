package com.example.hackhub.boundary;

import com.example.hackhub.boundary.dto.ValutazioneRequest;
import com.example.hackhub.controller.ValutazioneHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
La tua ValutazioneBoundary deve essere una classe Spring con
@RestController e un endpoint.
 */
@RestController
@RequestMapping("/api/sottomissioni")
public class ValutazioneBoundary {
    private final ValutazioneHandler handler;

    public ValutazioneBoundary(ValutazioneHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/{id}/valutazione")
    public ResponseEntity<Void> inserisciValutazione(@PathVariable("id") String idSottomissione, @RequestBody ValutazioneRequest request){
        handler.avviaInserimentoValutazione(idSottomissione, request.giudizio(), request.punteggio());
        return ResponseEntity.status(201).build();
    }
}
