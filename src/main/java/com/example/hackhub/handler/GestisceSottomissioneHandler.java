package com.example.hackhub.handler;

import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.repository.RepositoryIscrizioniTeam;
import com.example.hackhub.repository.RepositoryMembriTeam;
import com.example.hackhub.repository.RepositorySottomissioni;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;

@Service
public class GestisceSottomissioneHandler {

    //TODO in uml togliere la repo sottomissioni dal diagramma di progetto e dal sequence
    private final RepositoryIscrizioniTeam repositoryIscrizioniTeam;
    private final RepositoryMembriTeam repositoryMembriTeam;
    private final ServizioNotifiche servizioNotifiche;

    /**
     * Metodo che istanzia l'handler per la gestione delle sottomissioni
     * @param repositoryIscrizioniTeam la repo per le iscrizioni agli hackathon
     * @param repositoryMembriTeam la repo per i membri team
     * @param servizioNotifiche il servizio per l'invio delle notifiche
     */
    public GestisceSottomissioneHandler(RepositoryIscrizioniTeam repositoryIscrizioniTeam, RepositoryMembriTeam repositoryMembriTeam, ServizioNotifiche  servizioNotifiche) {
        this.repositoryIscrizioniTeam = repositoryIscrizioniTeam;
        this.repositoryMembriTeam = repositoryMembriTeam;
        this.servizioNotifiche = servizioNotifiche;
    }

    /**
     * Metodo che crea una sottomissione e la invia
     * @param nomeUtente il nome utente del membro che invia la sottomissione
     * @param link il link a un file online o a una repository di GitHub
     */
    public void inviaSottomissione(String nomeUtente, String link) {
        // Prelevo il membro del team e risalgo allo stato dell'hackathon
        MembroTeam membro = repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente)
                .orElseThrow(() -> new RuntimeException("Membro del team non trovato"));
        Team team = membro.getTeam();
        IscrizioneTeam iscrizioneTeam = repositoryIscrizioniTeam.findByTeam(team)
                .orElseThrow(() -> new RuntimeException("Team non trovato"));
        Hackathon hackathon = iscrizioneTeam.getHackathon();

        // Verifico che l'invio delle sottomissioni sia consentito, se non succede niente è tutto ok
        hackathon.getStato().verificaInvioSottomissioneConsentito(iscrizioneTeam.getHackathon());

        // Altrimenti creo la sottomissione, la aggiungo all'iscrizione, salvo tutto e notifico il resto del team
        Sottomissione sottomissione = new Sottomissione(link);
        iscrizioneTeam.aggiungiSottomissione(sottomissione);
        repositoryIscrizioniTeam.save(iscrizioneTeam);

        for (MembroTeam m : team.getMembri())
            if (!m.equals(membro))
                servizioNotifiche.creaNotifica(m.getUtente(), TipoNotifica.SOTTOMISSIONE_MODIFICATA, membro.getUtente().getNomeUtente() + " ha modificato la sottomissione dell'hackathon " + hackathon.getNome());
    }

    /**
     * Metodo che rimuove una sottomissione di un team
     * @param nomeUtente il nome utente del membro che attiva la rimozione della sottomissione
     */
    public void attivaRimozioneSottomissione(String nomeUtente) {
        // Prelevo il membro del team e risalgo allo stato dell'hackathon
        MembroTeam membro = repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente)
                .orElseThrow(() -> new RuntimeException("Membro del team non trovato"));
        Team team = membro.getTeam();
        IscrizioneTeam iscrizioneTeam = repositoryIscrizioniTeam.findByTeam(team)
                .orElseThrow(() -> new RuntimeException("Team non trovato"));
        Hackathon hackathon = iscrizioneTeam.getHackathon();

        // Verifico che la rimozione delle sottomissioni sia consentita, se non succede niente è tutto ok
        hackathon.getStato().verificaInvioSottomissioneConsentito(iscrizioneTeam.getHackathon());

        // Altrimenti rimuovo la sottomissione e notifico il resto del team
        iscrizioneTeam.rimuoviSottomissione();
        repositoryIscrizioniTeam.save(iscrizioneTeam);
        for (MembroTeam m : team.getMembri())
            if (!m.equals(membro))
                servizioNotifiche.creaNotifica(m.getUtente(), TipoNotifica.SOTTOMISSIONE_RIMOSSA, membro.getUtente().getNomeUtente() + " ha attivato la rimozione della sottomissione dell'hackathon " + hackathon.getNome());
    }
}
