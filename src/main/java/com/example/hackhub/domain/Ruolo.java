package com.example.hackhub.domain;

/**
 * Enum che contiene il ruolo di un generico Utente. Tramite questo enum, si possono distinguere i ruoli
 * seguenti, in modo da gestire anche autenticazioni e permessi diversi.
 */
public enum Ruolo {
    /**
     * Un Membro dello Staff che crea e gestisce gli Hackathon. Può creare Hackathon, stabilendone i vari
     * requisiti di ingresso e proprietà, e gestire anche i Membri dello Staff di ciascun hackathon che crea,
     * invitando altri Utenti come MENTORE oppure GIUDICE
     */
    ORGANIZZATORE,

    /**
     * Un Membro dello Staff che funge da supporto ai Team partecipanti a un Hackathon. Può anche prenotare
     * delle call con i Team per discutere su eventuali problematiche o dubbi nella preparazione delle
     * sottomissioni dei Team. Effettua anche una funzione di supervisione per assicurarsi che i Team
     * partecipanti rispettino le regole, e segnalando eventuali violazioni del regolamento all'ORGANIZZATORE
     */
    MENTORE,

    /**
     * Un Membro dello Staff incaricato di valutare le sottomissioni consegnate dai Team, per poi proclamare
     * il Team vincitore dell'Hackathon.
     */
    GIUDICE,

    /**
     * Un generico Utente che partecipa agli Hackathon. Può partecipare agli Hackathon solo se è Membro di
     * un Team, come Leader oppure come un Membro generico.
     */
    UTENTE
}
