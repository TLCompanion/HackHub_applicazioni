package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.StatoIscrizione;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoIscrizione stato;

    public IscrizioneTeam() {}

    /**
     * Crea un'iscrizione di un team
     * @param team il team associato all'iscrizione
     * @param idIscrizione l'identificativo dell'iscrizione
     * @param hackathon l'hackathon a cui è associata l'iscrizione
     * @param stato lo stato dell'iscrizione
     */
    public IscrizioneTeam(Team team, String idIscrizione, Hackathon hackathon, StatoIscrizione stato){
        this.idIscrizione = idIscrizione;
        this.team = team;
        this.hackathon = hackathon;
        this.stato = stato;
    }
}
