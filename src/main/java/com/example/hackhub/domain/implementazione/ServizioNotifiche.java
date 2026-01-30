package com.example.hackhub.domain.implementazione;

/**
 * Pattern: Singleton, gestione delle notifiche
 */
public class ServizioNotifiche {

    private ServizioNotifiche instance;

    /**
     * Costruzione di un'entitò di ServizioNotifiche
     */
    public ServizioNotifiche() {
    }

    /**
     * Se l'istanza è nulla ne creo una nuuova
     * @return l'istanza creata se è null o quella precedentemente esistente se non è nulla
     */
    public ServizioNotifiche getInstance() {
        if (instance == null) {
            instance = new ServizioNotifiche();
        }
        return instance;
    }

    /**
     * Invio di una richiesta da parte di un mentore, un'organizzatore o un team
     * @param richiesta la richiesta da inviare
     */
    public void inviaRichiesta (Richiesta richiesta){
        //TODO IMPLEMENTARE
    }

    /**
     * Invio di una notifica alla conclusione dell'hackathon
     * @param notifica la notifica da inviare
     */
    public void inviaNotifica(Notifica notifica){
        //TODO IMPLEMENTARE
    }
}
