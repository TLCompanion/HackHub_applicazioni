package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che gestisce un'hackathon e tutti i suoi elementi
 */
public class Hackathon implements Publisher {

    private String idHackathon;
    private String nome;
    private Periodo periodo;
    private BigDecimal premio;
    private String luogo;
    private int teamMax;
    private int teamMin;
    private String regolamento;
    private int maxIscrizioni;
    private StatoHackathon stato;
    private List<String> listaSottomissioni;
    private LocalDateTime scadenzaIscrizioni;
    private List<Subscriber> subscribers;
    private List<Staff> staff;
    private List<String> iscrizioni;

    /**
     * Costruisce un'hackathon
     * @param builder il costruttore dell'hackathon
     * @param idHackathon l'identificativo dell'hackathon
     * @param stato lo stato dell'hackathon
     */
    private Hackathon(HackathonBuilder builder, String idHackathon, StatoHackathon stato) {
        this.nome = builder.nome;
        this.periodo = builder.periodo;
        this.premio = builder.premio;
        this.luogo = builder.luogo;
        this.teamMax = builder.teamMax;
        this.teamMin = builder.teamMin;
        this.regolamento = builder.regolamento;

        // valori di default / inizializzazioni
        this.idHackathon = idHackathon;
        this.stato = stato;
        this.listaSottomissioni = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        this.staff = new ArrayList<>();
        this.iscrizioni = new ArrayList<>();
    }

    //metodi da implementare

    public void setStato(StatoHackathon stato) { this.stato = stato; }

    public int getTeamMax() {
        return teamMax;
    }

    public int getTeamMin() {
        return teamMin;
    }

    public void aggiungiIscrizione(String descrizione){
        //TODO IMPLEMENTARE
    }

    public void aggiungiValutazione(String idValutazione, String idSottomissione){
        //TODO IMPLEMENTARE
    }

    public String getInfo(){
        //TODO IMPLEMENTARE
        return null;
    }

    public void attach(Subscriber subscriber){
        //TODO IMPLEMENTARE
    }

    public void detach(Subscriber subscriber){
        //TODO IMPLEMENTARE
    }

    public void notify(TipoNotifica evento){
        //TODO IMPLEMENTARE
    }

    public StatoHackathon getStato(){ return this.stato; }
}
