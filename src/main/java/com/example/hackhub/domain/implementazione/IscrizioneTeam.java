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

    @Column(nullable = false)
    private String idHackathon;

    @Column(nullable = false)
    private String idTeam;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoIscrizione stato;

    public IscrizioneTeam() {}

    /**
     * Crea un'iscrizione di un team
     * @param idTeam l'identificativo del team che si sta iscrivendo all'hackathon
     * @param idIscrizione l'identificativo dell'iscrizione
     * @param idHackathon l'identificativo dell'hackthon a cui si stanno iscrivendo
     * @param stato lo stato dell'iscrizione
     */
    public IscrizioneTeam(String idTeam, String idIscrizione, String idHackathon, StatoIscrizione stato){
        this.idIscrizione = idIscrizione;
        this.idTeam = idTeam;
        this.idHackathon = idHackathon;
        this.stato = stato;
    }
}
