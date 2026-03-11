package com.example.hackhub.boundary;

import com.example.hackhub.handler.InvitaUtentiHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invitiUtenti")
public class InvitaUtentiBoundary {

    private final InvitaUtentiHandler handler;

    public InvitaUtentiBoundary(InvitaUtentiHandler handler){
        this.handler = handler;
    }
}
