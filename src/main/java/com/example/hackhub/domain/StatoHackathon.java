package com.example.hackhub.domain;

import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.eccezioni.TransizioneNonConsentitaException;

/**
 * interfaccia che gestisce lo stato dell'hackathon avviando o concludendo iscrizioni ed eventi
 */
public interface StatoHackathon {

    /**
     * Apre le iscrizioni per un certo Hackathon
     * @param hackathon l'evento considerato
     */
    default void apriIscrizioni(Hackathon hackathon){
        throw new TransizioneNonConsentitaException("apri iscrizioni non consentito");
    };

    /**
     * Chiude le iscrizioni per un certo Hackathon
     * @param hackathon l'evento considerato
     */
    default void chiudiIscrizioni(Hackathon hackathon){
        throw new TransizioneNonConsentitaException("chiudi iscrizioni non consentito");
    };

    /**
     * Avvio dell'hackathon
     * @param hackathon l'evento considerato
     */
    default void avviaHackathon(Hackathon hackathon){
        throw  new TransizioneNonConsentitaException("avvia hackathon non consentito");
    };

    /**
     * Conclusione dell'hackathon
     * @param hackathon l'evento considerato
     */
    default void concludiHackathon(Hackathon hackathon){
        throw  new TransizioneNonConsentitaException("concludi hackathon non consentito");
    };

    default void avviaValutazione(Hackathon hackathon){
        throw  new TransizioneNonConsentitaException("avvia valutazione non consentito");
    };

    // Permessi/azioni di business - “guardie”
    //TODO: aggiungere altre azioni consentite o non consentite a seconda dello stato e aggiornare diagramma UML
    default void verificaIscrizioneConsentita(Hackathon h) {
        throw new TransizioneNonConsentitaException("verificaIscrizione non consentito");
    }

    default void verificaInvioSottomissioneConsentito(Hackathon h) {
        throw new TransizioneNonConsentitaException("verificaInvioSottomissione non consentito");
    }

    default void verificaValutazioneConsentita(Hackathon h) {
        throw new TransizioneNonConsentitaException("verificaValutazione non consentito");
    }
}
