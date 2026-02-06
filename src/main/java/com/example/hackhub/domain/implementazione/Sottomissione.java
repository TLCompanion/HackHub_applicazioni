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

    //private String idValutazione
    @OneToOne
    @JoinColumn(name = "id_valutazione")
    private Valutazione valutazione;

    public Sottomissione() {}

    /**
     * Creazine di una nuova sottomissione di un team
     * @param idSottomissione l'identificativo della sottomissione
     * @param riferimentoFile il file allegato
     */
    public Sottomissione(String idSottomissione, String riferimentoFile) {
        this.idSottomissione = idSottomissione;
        this.riferimentoFile = riferimentoFile;
        this.valutazione = null;
        //all'inizio la valutazione non c'è
    }

    /**
     * Metodo che assegna una valutazione a questa sottomissione
     *
     * @param valutazione la valutazione da assegnare
     */
    //String idValutazione
    public void impostaValutazione(Valutazione valutazione) {
        this.valutazione = valutazione;
    }



    // METODI GETTER

    public String getIdSottomissione() { return idSottomissione; }

    public String getRiferimentoFile() { return riferimentoFile; }

    public String getIdValutazione() { return valutazione.getIdValutazione(); }
}
