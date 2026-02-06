package com.example.hackhub.controller;

import com.example.hackhub.boundary.CreaTeamBoundary;
import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.MembroTeam;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.domain.implementazione.Utente;
import com.example.hackhub.domain.implementazione.Validatore;
import com.example.hackhub.repository.RepositoryMembriTeam;
import com.example.hackhub.repository.RepositoryTeam;

public class CreaTeamHandler {

    private final RepositoryTeam repositoryTeam;
    private final RepositoryMembriTeam repositoryMembriTeam;
    private final Validatore validatore;
    private final CreaTeamBoundary boundary;

    /**
     * Costruisce un'entitò di CreaTeamHandler che gestisce la creazione di team
     * @param repositoryTeam la repository per controllare se il team già esiste
     * @param validatore per controllare se il nome del team è valido
     * @param boundary per confermare la creazione
     */
    public CreaTeamHandler(RepositoryTeam repositoryTeam, RepositoryMembriTeam repositoryMembriTeam, Validatore validatore, CreaTeamBoundary boundary) {
        this.repositoryTeam = repositoryTeam;
        this.repositoryMembriTeam = repositoryMembriTeam;
        this.validatore = validatore;
        this.boundary = boundary;
    }

    /**
     * Avvia la creazione di un team da parte di un'utente. Se l'utente ha già un team o è già membro la creazione
     * non va a buon fine, in tutti gli altri casi il membro del team viene associato al team con il ruolo di leader
     *
     * @param utente l'utente che sta avviando  la creazione di un team
     */
    public void avviaCreazioneTeam(Utente utente){

        //se il membro ha già un team all'interno della repository esce
        if (repositoryMembriTeam.esisteTeam(utente.getIdUtente())){
            return;
        }

        //altrimenti prende in considerazione una stringa per il nome
        boolean nomeDisponibile = false;
        String nome;

        //l'utente inserisce il nome, il validatore controlla se esiste già un team chiamato così
        do{
            nome = boundary.inserisciNome();
            nomeDisponibile = !validatore.verificaNomeTeam(nome);

            //se esiste e quindi il nome non è disponibile allora gli dice che non può usarlo altrimenti esce dal ciclo
            if (!nomeDisponibile){
                boundary.mostraErrore("Nome non disponibile");
            }
        }while (!nomeDisponibile);

        //una volta che ha il nome del team crea il team con quel nome e associa a quel team il membro del team con il ruolo di leader
        Team team = new Team(nome);
        MembroTeam membroTeam = new MembroTeam(utente, team, RuoloTeam.LEADER);

        repositoryTeam.save(team);
        repositoryMembriTeam.save(membroTeam);

        boundary.confermaCreazione();
    }
}
