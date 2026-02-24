package com.example.hackhub.controller;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.Concluso;
import com.example.hackhub.domain.implementazione.statePattern.IscrizioniChiuse;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.ForbiddenException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryIscrizioniTeam;
import com.example.hackhub.repository.RepositoryMembriTeam;
import com.example.hackhub.repository.RepositoryUtenti;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class IscriviTeamHandler {
    private final RepositoryMembriTeam repositoryMembriTeam;
    private final RepositoryHackathon repositoryHackathon;
    private final RepositoryIscrizioniTeam repositoryIscrizioniTeam;

    public IscriviTeamHandler(RepositoryMembriTeam repositoryMembriTeam, RepositoryHackathon repositoryHackathon, RepositoryIscrizioniTeam repositoryIscrizioniTeam) {
        this.repositoryMembriTeam = repositoryMembriTeam;
        this.repositoryHackathon = repositoryHackathon;
        this.repositoryIscrizioniTeam = repositoryIscrizioniTeam;
    }

    //TODO separare in più metodi privati per leggibilità e gestione
    @Transactional
    public void avviaIscrizioneHackathon(String idUtente, String nomeHackathon) {
        MembroTeam membroTeam = repositoryMembriTeam.findByUtente_IdUtente(idUtente).orElseThrow(() ->
                new NotFoundException("L'utente non è membro di nessun team"));
        if (!membroTeam.getRuolo().equals(RuoloTeam.LEADER)) {
            throw new ForbiddenException("L'utente non è il leader del team");
        }
        Team team = membroTeam.getTeam();
        Hackathon hackathon = repositoryHackathon.findByNome(nomeHackathon).orElseThrow(() ->
                new NotFoundException("Hackathon non trovato"));
        if (team.getNumMembri()<hackathon.getTeamMin() || team.getNumMembri()>hackathon.getTeamMax()) {
            throw new ConflictException("Il numero di membri del team non è compatibile con i requisiti dell'hackathon");
        }
        IscrizioneTeam iscrizioneEsistente = repositoryIscrizioniTeam.findByTeamAndHackathon(team, hackathon).
                orElse(null);
        if (iscrizioneEsistente != null) {
            if (iscrizioneEsistente.getHackathon().equals(hackathon)) {
                throw new ConflictException("Il team è già iscritto a questo hackathon");
            }
        }
        if (hackathon.getIscrizioni().size()>=hackathon.getMaxIscrizioni()) {
            throw new ConflictException("Il numero massimo di iscrizioni è già stato raggiunto");
        }
        IscrizioneTeam iscrizione = new IscrizioneTeam(team, hackathon);
        hackathon.aggiungiIscrizioneTeam(iscrizione);
        repositoryHackathon.save(hackathon);
        repositoryIscrizioniTeam.save(iscrizione);

        if (hackathon.getIscrizioni().size()==hackathon.getMaxIscrizioni()) {
            hackathon.setStato(IscrizioniChiuse.INSTANCE);
        }
    }
}
