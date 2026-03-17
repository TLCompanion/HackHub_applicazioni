package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.domain.implementazione.Utente;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryMembriTeam;
import com.example.hackhub.repository.RepositoryUtenti;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;

@Service
public class InvitaUtentiHandler {

    private final RepositoryUtenti repositoryUtenti;
    private final RepositoryMembriTeam repositoryMembriTeam;
    private final ServizioNotifiche servizioNotifiche;

    public InvitaUtentiHandler(RepositoryUtenti repositoryUtenti, RepositoryMembriTeam repositoryMembriTeam, ServizioNotifiche servizioNotifiche){
        this.repositoryUtenti = repositoryUtenti;
        this.repositoryMembriTeam = repositoryMembriTeam;
        this.servizioNotifiche = servizioNotifiche;
    }
    public void invitaUtenti(String nomeUtente, Team team) {
        Utente utente = repositoryUtenti.findByNomeUtente(nomeUtente)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        if (repositoryMembriTeam.findByUtente_NomeUtente(utente.getNomeUtente()).isPresent()) {
            throw new ConflictException("L'utente appartiene già a un team");
        }

        String leader = team.getMembri().stream()
                .filter(membro -> membro.getRuolo() == RuoloTeam.LEADER)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Leader del team non trovato"))
                .getUtente()
                .getNomeUtente();

        servizioNotifiche.creaInvitoTeam(leader, utente, team);
    }
}
