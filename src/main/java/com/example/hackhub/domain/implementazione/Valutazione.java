package com.example.hackhub.domain.implementazione;

import com.example.hackhub.repository.RepositoryValutazioni;
import jakarta.persistence.*;

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

    //la uso per generare l'id della valutazione, non è un campo che deve essere salvato nel DB
    private RepositoryValutazioni repository;

    public Valutazione() {}

    /**
     * Creazione di una valutazione per una sottomissione
     * @param voto il voto assegnato dal Giudice
     * @param descrizione la motivazione del voto assegnato
     */
    public Valutazione(int voto, String descrizione) {
        this.voto = voto;
        this.descrizione = descrizione;
        int numeroValutazioni = repository.findAll().size();
        this.idValutazione = "v" + (numeroValutazioni + 1); // Generazione dell'id della valutazione in base al numero di valutazioni già presenti
    }




    // METODI GETTER E SETTER

    public String getIdValutazione() { return idValutazione; }

    public int getVoto() { return voto; }

    public String getDescrizione() { return descrizione; }

    public void setIdValutazione(String idValutazione) { this.idValutazione = idValutazione; }

    public void setVoto(int voto) { this.voto = voto; }

    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
}
