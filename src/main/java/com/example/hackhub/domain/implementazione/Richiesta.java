package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.StatoRichiesta;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che gestisce gli elementi generali di una richiesta
 */
public class Richiesta {

    private String idRichiesta;
    private String idMittente;
    private List<String> idDestinatari;
    private StatoRichiesta stato;

    /**
     * Creazione di una nuova richiesta
     * @param idRichiesta l'identificativo della richiesta
     * @param idMittente l'identificativo del mittente
     * @param stato lo stato della richiesta
     */
    public Richiesta(String idRichiesta, String idMittente, StatoRichiesta stato) {
        this.idRichiesta = idRichiesta;
        this.idMittente = idMittente;
        this.idDestinatari = new ArrayList<>();
        this.stato = StatoRichiesta.INVIATO; //all'inizio quando ancora la richiesta non è stata valutata lo stato è sempre inviato
    }
}
