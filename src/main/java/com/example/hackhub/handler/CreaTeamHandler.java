package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.ForbiddenException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryMembriTeam;
import com.example.hackhub.repository.RepositoryTeam;
import com.example.hackhub.repository.RepositoryUtenti;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CreaTeamHandler {

    private final RepositoryTeam repositoryTeam;
    private final RepositoryMembriTeam repositoryMembriTeam;
    private final RepositoryUtenti repositoryUtenti;

    /**
     * Costruisce un'entità di CreaTeamHandler che gestisce la creazione di team
     *
     * @param repositoryTeam       la repository per controllare se il nome del team già esiste
     * @param repositoryMembriTeam la repository per controllare se l'utente è già membro di un team
     * @param repositoryUtenti     la repository per recuperare l'utente che vuole creare il team
     */
    public CreaTeamHandler(RepositoryTeam repositoryTeam, RepositoryMembriTeam repositoryMembriTeam, RepositoryUtenti
            repositoryUtenti) {
        this.repositoryTeam = repositoryTeam;
        this.repositoryMembriTeam = repositoryMembriTeam;
        this.repositoryUtenti = repositoryUtenti;
    }

    /**
     * Avvia la creazione di un team, verificando che l'utente non sia già membro di un team e che il nome del team
     * non sia già esistente. Se tutte le verifiche passano, crea un nuovo team e aggiunge l'utente come membro con
     * ruolo di leader.
     *
     * @param nomeUtente il nome utente dell'utente che vuole creare il team
     * @param nomeTeam il nome del team da creare
     */
    @Transactional
    public void avviaCreazioneTeam(String nomeUtente, String nomeTeam) {
        Utente utente = repositoryUtenti.findByNomeUtente(nomeUtente).orElseThrow(() ->
                new NotFoundException("Utente non trovato"));
        if (repositoryMembriTeam.existsByUtente(utente)) {
            throw new ForbiddenException("L'utente è già membro di un team");
        }
        if (repositoryTeam.existsByNome(nomeTeam)) {
            throw new ConflictException("Esiste già un team con questo nome");
        }
        Team team = new Team(nomeTeam);
        MembroTeam leader = new MembroTeam(utente, team, RuoloTeam.LEADER);
        team.setLeader(leader);
        repositoryTeam.save(team);
    }
}
