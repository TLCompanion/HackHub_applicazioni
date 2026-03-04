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
     * I team vengono notificati delle proposte di call inviate dal mentore
     */
    PROPOSTA_CALL,

    /**
     * Gli utenti vengono notificati degli inviti per diventare staff per un'hackathon inviati dall'organizzatore
     */
    INVITO_STAFF,

    /**
     * Un qualsiasi tipo di notifica non specificata
     */
    NOTIFICA_GENERICA
}
