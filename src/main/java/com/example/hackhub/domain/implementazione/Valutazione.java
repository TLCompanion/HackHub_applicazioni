package com.example.hackhub.domain.implementazione;

/**
 * Valutazione fatta da parte di un giudice per una sottomissione inviata
 * da un' team ad un'hackathon
 */
public class Valutazione {

    private String idValutazione;
    private int voto;
    private String descrizione;

    /**
     * Creazione di una valutazione per una sottomissione
     * @param idValutazione l'id della valutazione
     * @param voto il voto assegnato dal Giudice
     * @param descrizione la motivazione del voto assegnato
     */
    public Valutazione(String idValutazione, int voto, String descrizione) {
        this.idValutazione = idValutazione;
        this.voto = voto;
        this.descrizione = descrizione;
    }
}
