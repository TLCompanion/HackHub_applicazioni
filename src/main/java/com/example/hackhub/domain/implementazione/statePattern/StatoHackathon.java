package com.example.hackhub.domain.implementazione.statePattern;

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
        throw new TransizioneNonConsentitaException("Non è possibile aprire le iscrizioni nella fase attuale");
    };

    /**
     * Chiude le iscrizioni per un certo Hackathon
     * @param hackathon l'evento considerato
     */
    default void chiudiIscrizioni(Hackathon hackathon){
        throw new TransizioneNonConsentitaException("Non è possibile chiudere le iscrizioni nella fase attuale");
    };

    /**
     * Avvio dell'hackathon
     * @param hackathon l'evento considerato
     */
    default void avviaHackathon(Hackathon hackathon){
        throw  new TransizioneNonConsentitaException("Non è possibile avviare l'hackathon nella fase attuale");
    };

    /**
     * Conclusione dell'hackathon
     * @param hackathon l'evento considerato
     */
    default void concludiHackathon(Hackathon hackathon){
        throw  new TransizioneNonConsentitaException("Non è possibile concludere l'hackathon nella fase attuale");
    };

    default void avviaValutazione(Hackathon hackathon){
        throw  new TransizioneNonConsentitaException("Non è possibile avviare la valutazione dell'hackathon nella fase attuale");
    };

    // Permessi/azioni di business - “guardie”
    //TODO: aggiungere altre azioni consentite o non consentite a seconda dello stato e aggiornare diagramma UML
    default void verificaIscrizioneConsentita(Hackathon h) {
        throw new TransizioneNonConsentitaException("Non è possibile iscriversi in questa fase dell'hackathon");
    }

    default void verificaInvioSottomissioneConsentito(Hackathon h) {
        throw new TransizioneNonConsentitaException("Non è possibile inviare sottomissioni in questa fase dell'hackathon");
    }

    default void verificaValutazioneConsentita(Hackathon h) {
        throw new TransizioneNonConsentitaException("Non è possibile valutare le sottomissioni in questa fase dell'hackathon");
    }
}
