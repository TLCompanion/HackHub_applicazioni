package com.example.hackhub.domain;

/**
 * Possibili stati di un'iscrizione effettuata da un team ad un hackathon
 */
public enum StatoIscrizione {

    /**
     * Se il team ha inviato un'iscrione ed è stata accettata
     */
    ATTIVA,

    /**
     * Se il team non ha terminato la sua iscrizione
     */
    ANNULLATA,

    /**
     * Se il team è stato espulso dall'hackathon
     */
    ESPULSA
}
