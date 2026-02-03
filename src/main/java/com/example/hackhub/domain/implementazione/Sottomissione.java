package com.example.hackhub.domain.implementazione;

import jakarta.persistence.*;

/**
 * Sottomissione creata dai team per un'hackathon
 */
@Entity
@Table(name = "sottomissioni")
public class Sottomissione {

    @Id
    @Column(nullable = false, updatable = false)
    private String idSottomissione;

    @Column(nullable = false)
    private String riferimentoFile; //allegato al file con il progetto richiesto dall'hackathon

    @Column
    private String idValutazione;

    public Sottomissione() {}

    /**
     * Creazine di una nuova sottomissione di un team
     * @param idSottomissione l'identificativo della sottomissione
     * @param riferimentoFile il file allegato
     * @param idValutazione l'identificativo della valutazione associata
     */
    public Sottomissione(String idSottomissione, String riferimentoFile, String idValutazione) {
        this.idSottomissione = idSottomissione;
        this.riferimentoFile = riferimentoFile;
        this.idValutazione = "";
    }

    /**
     * Metodo che assegna una valutazione a questa sottomissione
     *
     * @param idValutazione la valutazione da assegnare
     */
    public void impostaValutazione(String idValutazione) {
        this.idValutazione = idValutazione;
    }



    // METODI GETTER

    public String getIdSottomissione() { return idSottomissione; }

    public String getRiferimentoFile() { return riferimentoFile; }

    public String getIdValutazione() { return idValutazione; }
}
