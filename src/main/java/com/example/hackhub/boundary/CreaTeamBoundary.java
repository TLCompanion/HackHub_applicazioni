package com.example.hackhub.boundary;

import com.example.hackhub.controller.CreaTeamHandler;

public class CreaTeamBoundary {

    public CreaTeamBoundary(CreaTeamHandler handler) {
        this.handler = handler;
    }

    private final CreaTeamHandler handler;

    public void avviaCreazioneTeam(String idUtente){
        this.handler.avviaCreazioneTeam(idUtente);
    }

    public String inserisciNome(){
        return "NomeTeam";
    }

    public void mostraErrore(String msg){
        System.out.println(msg);
    }

    public void confermaCreazione(){
        System.out.println("Team creato");
    }

}
