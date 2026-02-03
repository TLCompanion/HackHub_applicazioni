package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// TODO persistenza sospesa

/**
 * Classe che gestisce un'hackathon e tutti i suoi elementi
 */
@Entity
@Table(name = "hackathon", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
public class Hackathon implements Publisher {

    @Id
    private String idHackathon;

    private String nome;

    @Embedded
    private Periodo periodo;

    private BigDecimal premio;

    private String luogo;

    private int teamMax;

    private int teamMin;

    @Lob
    private String regolamento;

    private int maxIscrizioni;

    @Transient
    private StatoHackathon stato;

    @Transient
    private List<String> listaSottomissioni;

    private LocalDateTime scadenzaIscrizioni;

    @Transient
    private List<Subscriber> subscribers;

    @Transient
    private List<Staff> staff;

    @Transient
    private List<String> iscrizioni;

    public Hackathon() {}

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
