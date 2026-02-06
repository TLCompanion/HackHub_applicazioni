package com.example.hackhub.boundary;

import com.example.hackhub.controller.CreaHackathonHandler;
import com.example.hackhub.controller.CreaTeamHandler;

public class CreaHackathonBoundary {

    private final CreaHackathonHandler handler;

    public CreaHackathonBoundary(CreaHackathonHandler handler) {
        this.handler = handler;
    }

    public void avviaCreazioneHackthon(){
        this.handler.avviaCreazioneHackathon();
    }

    public String inserisciNome(){
        return "NomeHackathon";
    }

    public void mostraErrore(String msg){
        System.out.println(msg);
    }

    public void confermaCreazione(){
        System.out.println("Hackathon creato");
    }

}
