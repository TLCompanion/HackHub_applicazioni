package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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

    private LocalDateTime scadenzaIscrizioni;

    @Transient
    private List<Subscriber> subscriber;

    @Transient
    private List<Staff> staff;

    //private List<String>
    @Transient
    private List<IscrizioneTeam> iscrizioni;

    public Hackathon() {
    }

    public Hackathon(String nome, Periodo periodo, BigDecimal premio, String luogo, int teamMax, int teamMin, String regolamento) {
        this.nome = nome;
        this.periodo = periodo;
        this.premio = premio;
        this.luogo = luogo;
        this.teamMax = teamMax;
        this.teamMin = teamMin;
        this.regolamento = regolamento;

        // valori di default / inizializzazioni
        // esempio: scadenza iscrizioni 1 giorno prima della fine dell'hackathon
        this.scadenzaIscrizioni = periodo.getDataFine().minusDays(1).atStartOfDay();
        this.idHackathon = "h" + UUID.randomUUID(); // genera un id univoco per l'hackathon
        this.stato = stato;
        this.subscriber = new ArrayList<>();
        this.staff = new ArrayList<>();
        this.iscrizioni = new ArrayList<>();
    }

    //metodi da implementare

    public void setStato(StatoHackathon stato) {
        this.stato = stato;
    }

    public int getTeamMax() {
        return teamMax;
    }

    public int getTeamMin() {
        return teamMin;
    }

    public void aggiungiIscrizione(IscrizioneTeam iscrizione) {
        this.iscrizioni.add(iscrizione);
    }

    public String getInfo() {
        return this.regolamento;
    }

    public void attach(Subscriber subscriber) {
        //TODO IMPLEMENTARE
    }

    public void detach(Subscriber subscriber) {
        //TODO IMPLEMENTARE
    }

    public void notify(TipoNotifica evento) {
        //TODO IMPLEMENTARE
    }

    public StatoHackathon getStato() {
        return this.stato;
    }

    public String getIdHackathon() {
        return this.idHackathon;
    }

    public String getNome() {
        return this.nome;
    }

    // mi serve per ottenerlo nell'handler delle valutazioni per verificare che il giudice è un giudice di quello specifico hackathon e non un giudice di un altro hacakthon
    public List<Staff> getStaff() {
        return this.staff;
    }

}
