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
    @OneToOne(optional = false)
    @JoinColumn(name = "hackathon_id")
    private Hackathon hackathon;

    //private String idTeam
    @OneToOne(optional = false)
    @JoinColumn(name = "id_team")
    private Team team;

    //TODO aggiungere riferimento alla sottomissione, poi nell'handler modificare il modo in cui si prende la
    // sottomissione (attualmente si prende da hackathon)
    @OneToOne(optional = false)
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
    @PrePersist
    private void assegnaId() {
        if (this.idIscrizione == null) {
            this.idIscrizione = "I-" + UUID.randomUUID();
        }
    }

    public Team getTeam() { return team; }

    public Hackathon getHackathon() { return hackathon; }
}
