package com.example.hackhub.boundary;

import com.example.hackhub.handler.InvitaUtentiHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inviti")
public class InvitaUtentiBoundary {

    private final InvitaUtentiHandler handler;

    public InvitaUtentiBoundary(InvitaUtentiHandler handler){
        this.handler = handler;
    }

    //Sul metodo che lo crea ci andrà poi @PostMapping("/utenti")
}
