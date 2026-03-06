package com.example.hackhub.servizi;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.TipoRichiesta;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryNotifica;
import com.example.hackhub.repository.RepositoryRichiesta;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Pattern: Singleton, gestione delle notifiche
 */
@Service
public class ServizioNotifiche {

    private final RepositoryRichiesta repositoryRichiesta;
    private final RepositoryNotifica repositoryNotifica;

    /**
     * Crea una nuovo servizio notifiche
     * @param repositoryNotifica il repository delle notifiche
     * @param repositoryRichiesta il repository delle richieste
     */
    public ServizioNotifiche(RepositoryNotifica repositoryNotifica, RepositoryRichiesta repositoryRichiesta){
        this.repositoryNotifica = repositoryNotifica;
        this.repositoryRichiesta = repositoryRichiesta;
    }

    /**
     * Crea una nuova notifica
     * @param destinatari i destinatari
     * @param tipo il tipo della notifica
     * @param messaggio il messaggio da inviare
     */
    public void creaNotifica(List<Utente> destinatari, TipoNotifica tipo, String messaggio){
        for (Utente d : destinatari) {
            Notifica notifica = new Notifica(messaggio, d, tipo);
            repositoryNotifica.save(notifica);
        }
    }

    /**
     * Invia una richiesta da parte di un mentore, un'organizzatore o un team
     * @param mittente il mittente associato
     * @param destinatari i destinatari
     * @param messaggio il messaggio della richiesta
     */
    public void creaRichiesta(String mittente, List<Utente> destinatari, TipoRichiesta tipo, String messaggio, Periodo periodo){
        for (Utente d : destinatari) {
            Richiesta richiesta = new Richiesta(mittente, messaggio, TipoRichiesta.PROPOSTA_CALL, d, periodo);
            repositoryRichiesta.save(richiesta);
        }
    }
}
