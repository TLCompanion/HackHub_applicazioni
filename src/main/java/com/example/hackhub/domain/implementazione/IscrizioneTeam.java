package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.StatoIscrizione;

/**
 * Classe che gestisce l'iscrizione di un team ad un'hackathon
 */
public class IscrizioneTeam {

    private String idIscrizione;
    private String idHackathon;
    private String idTeam;
    private StatoIscrizione stato;

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
