package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.StatoRichiesta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe che gestisce gli elementi generali di una richiesta
 */
public class Richiesta<T> {

    private String idRichiesta;
    private T mittente;
    private Collection<T> destinatari;
    private StatoRichiesta stato;

    /**
     * Creazione di una nuova richiesta
     * @param idRichiesta l'identificativo della richiesta
     * @param mittente il mittente della richiesta
     */
    public Richiesta(String idRichiesta, T mittente) {
        this.idRichiesta = idRichiesta;
        this.mittente = mittente;
        this.destinatari = new ArrayList<>();
        this.stato = StatoRichiesta.INVIATO; //all'inizio quando ancora la richiesta non è stata valutata lo stato è sempre inviato
    }



    // METODI GETTER


    public String getIdRichiesta() { return idRichiesta; }

    public StatoRichiesta getStato() { return stato; }
}
