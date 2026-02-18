package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloStaff;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Pattern: Singleton, gestione delle notifiche
 */
//TODO non so se serve l'implementazione del pattern singleton con @Service, da verificare
@Service
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
     * Invia una richiesta da parte di un mentore, un'organizzatore o un team
     */
    public void inviaInvitoStaff (Hackathon hackathon, Map<Utente, RuoloStaff> destinatari){
        //TODO IMPLEMENTARE
    }

    public void inviaPropostaCall (){
        //TODO IMPLEMENTARE
    }

    /**
     * Invia una notifica a un utente, un team o un'organizzazione
     */
    public void inviaNotifica(){
        //TODO IMPLEMENTARE
    }
}
