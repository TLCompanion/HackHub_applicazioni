package com.example.hackhub.servizi;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.repository.RepositoryNotifica;
import com.example.hackhub.repository.RepositoryRichiesta;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
     * Crea una proposta di call
     * @param nomeMittente
     * @param destinatario
     * @param periodo la durata della call
     */
    public void creaPropostaCall(String nomeMittente, Utente destinatario, Periodo periodo) {
        PropostaCall propostaCall = new PropostaCall(nomeMittente,
                "Proposta di Call",
                destinatario,
                LocalDateTime.of(periodo.getDataInizio().minusDays(1), periodo.getOraInizio()),
                periodo);
        repositoryRichiesta.save(propostaCall);
    }

    /**
     * Metodo che istanzia un Invito allo Staff di un hackathon
     * @param nomeMittente
     * @param destinatario
     * @param hackathon
     * @param ruolo il ruolo offerto
     */
    public void creaInvitoStaff(String nomeMittente, Utente destinatario, Hackathon hackathon, RuoloStaff ruolo) {
        if (ruolo.equals(RuoloStaff.ORGANIZZATORE))
            throw new IllegalArgumentException("Ruolo non assegnabile");

        InvitoStaff invitoStaff = new InvitoStaff(
                nomeMittente,
                "Invito nello Staff di " + hackathon.getNome(),
                destinatario,
                LocalDateTime.now(),
                hackathon,
                ruolo);
        repositoryRichiesta.save(invitoStaff);
    }

    public void creaInvitoTeam(String nomeMittente, Utente destinatario, Team team) {
        InvitoTeam invitoTeam = new InvitoTeam(
                nomeMittente,
                "Invito ad entrare nel team: " + team.getNome(),
                destinatario,
                LocalDateTime.now().plusDays(3),
                team
        );
        repositoryRichiesta.save(invitoTeam);
    }
}
