package com.example.hackhub.boundary;

import com.example.hackhub.controller.CreaTeamHandler;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.domain.implementazione.Utente;

public class CreaTeamBoundary {

    public CreaTeamBoundary(CreaTeamHandler handler) {
        this.handler = handler;
    }

    private final CreaTeamHandler handler;

    public void avviaCreazioneTeam(Utente utente){
        this.handler.avviaCreazioneTeam(utente);
    }

    public Team inserisciTeam(){
        return new Team();
    }

    public void mostraErrore(String msg){
        System.out.println(msg);
    }

    public void confermaCreazione(){
        System.out.println("Team creato");
    }

}
