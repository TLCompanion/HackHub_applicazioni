package com.example.hackhub.controller;

import com.example.hackhub.boundary.CreaHackathonBoundary;
import com.example.hackhub.domain.implementazione.HackathonBuilder;
import com.example.hackhub.domain.implementazione.Utente;

public class CreaHackathonHandler {

    private final CreaHackathonBoundary boundary;
    private HackathonBuilder builder;

    public CreaHackathonHandler(CreaHackathonBoundary creaHackathonBoundary) {
        this.boundary = creaHackathonBoundary;
    }

    public void avviaCreazioneHackathon(){
        boundary.avviaCreazioneHackthon();
    }

    public void invitaStaff(){

    }

    //String idUtente
    public void impostaOrganizzatore(Utente utente){

    }
}
