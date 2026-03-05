package com.example.hackhub.observer;

import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.repository.RepositoryIscrizioniTeam;
import com.example.hackhub.repository.RepositoryTeam;
import com.example.hackhub.servizi.ServizioNotifiche;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Listener per la gestione delle richieste
 */
public class HackathonListener implements Subscriber {

    private final RepositoryIscrizioniTeam repositoryIscrizioniTeam;
    private final ServizioNotifiche servizioNotifiche;

    public HackathonListener(RepositoryIscrizioniTeam repositoryIscrizioniTeam,
                             ServizioNotifiche servizioNotifiche) {
        this.repositoryIscrizioniTeam = repositoryIscrizioniTeam;
        this.servizioNotifiche = servizioNotifiche;
    }

    @Override
    public void update(Evento evento) {
        switch (evento.tipo){
            case VALUTAZIONE_CONCLUSA -> gestisciValutazioneConclusa(evento);
        }
    }

    private void gestisciValutazioneConclusa(Evento evento) {
        List<IscrizioneTeam> iscrizioni = repositoryIscrizioniTeam.findAllByHackathon(
                (Hackathon) evento.data.get("hackathon"));
        Set<Utente> destinatariSet = new HashSet<>();
        for (IscrizioneTeam iscrizione : iscrizioni) {
            Team team = iscrizione.getTeam();
            for (MembroTeam m : team.getMembri()) {
                destinatariSet.add(m.getUtente());
            }
        }
        chiamaServizioNotifiche(destinatariSet, evento.getTipo());
    }

    private void chiamaServizioNotifiche(Set<Utente> destinatariSet, TipoNotifica tipo) {
        List<Utente> destinatari = new ArrayList<>(destinatariSet);
        servizioNotifiche.inviaNotifica(destinatari, tipo);
    }
}
