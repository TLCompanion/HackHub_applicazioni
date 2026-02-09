package com.example.hackhub.domain.implementazione;

import com.example.hackhub.repository.RepositoryValutazioni;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Valutazione fatta da parte di un giudice per una sottomissione inviata
 * da un' team ad un'hackathon
 */
@Entity
@Table(name = "valutazioni")
public class Valutazione {

    @Id
    @Column(nullable = false, updatable = false)
    private String idValutazione;

    @Column(nullable = false)
    private int voto;

    @Column
    private String descrizione;

    public Valutazione() {}

    /**
     * Creazione di una valutazione per una sottomissione
     * @param voto il voto assegnato dal Giudice
     * @param descrizione la motivazione del voto assegnato
     */
    public Valutazione(int voto, String descrizione) {
        this.voto = voto;
        this.descrizione = descrizione;
        this.idValutazione = "v" + UUID.randomUUID(); //generazione di un id univoco per la valutazione
    }




    // METODI GETTER E SETTER

    public String getIdValutazione() { return idValutazione; }

    public int getVoto() { return voto; }

    public String getDescrizione() { return descrizione; }

    public void setVoto(int voto) { this.voto = voto; }

    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
}
