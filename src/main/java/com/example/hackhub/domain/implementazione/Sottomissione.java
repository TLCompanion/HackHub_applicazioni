package com.example.hackhub.domain.implementazione;

/**
 * Sottomissione creata dai team per un'hackathon
 */
public class Sottomissione {

    private String idSottomissione;
    private String riferimentoFile; //allegato al file con il progetto richiesto dall'hackathon
    private String idValutazione;

    public Sottomissione(String idSottomissione, String riferimentoFile, String idValutazione) {
        this.idSottomissione = idSottomissione;
        this.riferimentoFile = riferimentoFile;
        this.idValutazione = idValutazione;
    }

    public void impostaValutazione(String idValutazione){
        //TODO IMPLEMENTARE
    }
}
