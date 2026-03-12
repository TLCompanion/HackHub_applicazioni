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

    /**
     * I membri del team vengono notificati, in quanto un membro del team ha modificato la sottomissione
     * consegnata in precedenza durante l'esecuzione di un hackathon, ad esempio per correggere un errore
     * o per migliorare la qualità del progetto consegnato.
     */
    MODIFICHE_SOTTOMISSIONE
}
