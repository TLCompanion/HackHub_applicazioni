package com.example.hackhub.controller;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.MembroTeam;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.domain.implementazione.Utente;
import com.example.hackhub.repository.RepositoryMembriTeam;
import com.example.hackhub.repository.RepositoryTeam;
import com.example.hackhub.repository.RepositoryUtenti;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
     * @param idUtente l'ID dell'utente che vuole creare il team
     * @param nomeTeam il nome del team da creare
     */
    @Transactional
    public void avviaCreazioneTeam(String idUtente, String nomeTeam) {
        if (repositoryMembriTeam.existsByIdUtente(idUtente)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Hai già un team");
        }
        if (repositoryTeam.existsByNome(nomeTeam)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nome del team già esistente");
        }
        Team team = new Team(nomeTeam);
        repositoryTeam.save(team);
        Utente utente = repositoryUtenti.findById(idUtente).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato"));
        MembroTeam membroTeam = new MembroTeam(utente, team, RuoloTeam.LEADER);
        repositoryMembriTeam.save(membroTeam);
    }

}
