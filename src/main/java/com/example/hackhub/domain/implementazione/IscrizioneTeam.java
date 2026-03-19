package com.example.hackhub.domain.implementazione;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Classe che gestisce l'iscrizione di un team ad un'hackathon
 */
@Entity
@Table(name = "iscrizioneTeam")
public class IscrizioneTeam {

    @Id
    @Column(nullable = false, updatable = false)
    private String idIscrizione;

    //private String idHackathon
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_hackathon", nullable = false)
    private Hackathon hackathon;

    //private String idTeam
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_team", nullable = false)
    private Team team;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_sottomissione")
    private Sottomissione sottomissione;

    public IscrizioneTeam() {}

    /**
     * Crea un'iscrizione di un team
     * @param team il team associato all'iscrizione
     * @param hackathon l'hackathon a cui è associata l'iscrizione
     */
    public IscrizioneTeam(Team team, Hackathon hackathon){
        this.team = team;
        this.hackathon = hackathon;
    }

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco all'iscrizione se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.

    /**
     * Assegna un id univoco ad ogni iscrizione
     */
    @PrePersist
    private void assegnaId() {
        if (this.idIscrizione == null) {
            this.idIscrizione = "I-" + UUID.randomUUID();
        }
    }

    /**
     * Metodo che inserisce una nuova sottomissione, se non è presente
     * @param sottomissione la sottomissione da allegare a questa iscrizione
     */
    public void aggiungiSottomissione(Sottomissione sottomissione) {
        if (this.sottomissione != null)
            throw new IllegalStateException("Sottomissione già presente per questa iscrizione.");
        this.sottomissione = sottomissione;
    }

    public String getId() { return idIscrizione; }

    public Team getTeam() { return team; }

    public Hackathon getHackathon() { return hackathon; }

    public Sottomissione getSottomissione() { return sottomissione; }

    public void setHackathon(Hackathon hackathon) {
        this.hackathon = hackathon;
    }
}
