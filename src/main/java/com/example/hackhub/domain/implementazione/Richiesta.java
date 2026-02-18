package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.StatoRichiesta;
import jakarta.persistence.PrePersist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Classe che gestisce gli elementi generali di una richiesta
 */
//TODO da sistemare tipo aggiungere i destinatari
public abstract class Richiesta<T> {

    private String idRichiesta;
    private String nomeMittente;
    private Collection<Utente> destinatari;
    private StatoRichiesta stato;

    /**
     * Creazione di una nuova richiesta
     * @param nomeMittente il mittente della richiesta
     */
    public Richiesta(String nomeMittente) {
        this.nomeMittente = nomeMittente;
        this.destinatari = new ArrayList<>();
        this.stato = StatoRichiesta.INVIATO; //all'inizio quando ancora la richiesta non è stata valutata lo stato è sempre inviato
    }

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco alla richiesta se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.
    @PrePersist
    private void assegnaId() {
        if (this.idRichiesta == null) {
            this.idRichiesta = "R-" + UUID.randomUUID();
        }
    }

    // METODI GETTER


    public String getIdRichiesta() { return idRichiesta; }

    public StatoRichiesta getStato() { return stato; }
}
