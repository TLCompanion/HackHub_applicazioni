package com.example.hackhub.domain;

/**
 * Tipi di notifiche inviati ai team e ai rispettivi membri
 */
public enum TipoNotifica {

    /**
     * I team vengono notificati che la valutazione dell'hackathon o della sottomissione è stata conclusa
     */
    VALUTAZIONE_CONCLUSA,

    /**
     * Il mittente viene notificato che il destinatario ha rifiutato la richiesta inviata
     */
    RIFIUTO_RICHIESTA,

    /**
     * Il mittente viene notificato che il destinatario ha accettato la richiesta inviata
     */
    ACCETTA_RICHIESTA,
}
