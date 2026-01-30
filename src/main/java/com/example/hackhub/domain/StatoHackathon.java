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
    void apriIscrizioni(Hackathon hackathon);

    /**
     * Chiude le iscrizioni per un certo Hackathon
     * @param hackathon l'evento considerato
     */
    void chiudiIscrizioni(Hackathon hackathon);

    /**
     * Avviamento dell'hackathon
     * @param hackathon l'evento considerato
     */
    void avvia(Hackathon hackathon);

    /**
     * Conclusione dell'hackathon
     * @param hackathon l'evento considerato
     */
    void avviaValutazione(Hackathon hackathon);
}
