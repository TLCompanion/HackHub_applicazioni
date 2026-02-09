package com.example.hackhub.domain.implementazione;

import jakarta.persistence.*;

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

    public IscrizioneTeam() {}

    /**
     * Crea un'iscrizione di un team
     * @param team il team associato all'iscrizione
     * @param idIscrizione l'identificativo dell'iscrizione
     * @param hackathon l'hackathon a cui è associata l'iscrizione
     */
    public IscrizioneTeam(Team team, String idIscrizione, Hackathon hackathon){
        this.idIscrizione = idIscrizione;
        this.team = team;
        this.hackathon = hackathon;
    }
}
