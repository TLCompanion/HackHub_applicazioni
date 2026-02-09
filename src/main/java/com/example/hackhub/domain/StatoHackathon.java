package com.example.hackhub.domain;

import com.example.hackhub.domain.implementazione.Hackathon;

/**
 * interfaccia che gestisce lo stato dell'hackathon avviando o concludendo iscrizioni ed eventi
 */
public interface StatoHackathon {

    /**
     * Apre le iscrizioni per un certo Hackathon
     * @param hackathon l'evento considerato
     */
    default void apriIscrizioni(Hackathon hackathon){
        throw new RuntimeException("Transizione non consentita: apri iscrizioni");
    };

    /**
     * Chiude le iscrizioni per un certo Hackathon
     * @param hackathon l'evento considerato
     */
    default void chiudiIscrizioni(Hackathon hackathon){
        throw new RuntimeException("Transizione non consentita: chiudi iscrizioni");
    };

    /**
     * Avvio dell'hackathon
     * @param hackathon l'evento considerato
     */
    default void avviaHackathon(Hackathon hackathon){
        throw  new RuntimeException("Transizione non consentita: avvia");
    };

    /**
     * Conclusione dell'hackathon
     * @param hackathon l'evento considerato
     */
    default void concludiHackathon(Hackathon hackathon){
        throw  new RuntimeException("Transizione non consentita: concludi hackathon");
    };

    default void avviaValutazione(Hackathon hackathon){
        throw  new RuntimeException("Transizione non consentita: avvia valutazione");
    };

    // Permessi/azioni di business - “guardie”
    //TODO: aggiungere altre azioni consentite o non consentite a seconda dello stato e aggiornare diagramma UML
    default void verificaIscrizioneConsentita(Hackathon h) {
        throw new RuntimeException("Transizione non consentita: iscrizione");
    }

    default void verificaInvioSottomissioneConsentito(Hackathon h) {
        throw new RuntimeException("Transizione non consentita: invio sottomissione");
    }

    default void verificaValutazioneConsentita(Hackathon h) {
        throw new RuntimeException("Transizione non consentita: valutazione");
    }
}
