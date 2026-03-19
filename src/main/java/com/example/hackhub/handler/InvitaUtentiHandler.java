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

    /**
     * Crea un istanza dell'handler per invitare gli utenti
     * @param repositoryUtenti la repository degli utenti
     * @param repositoryMembriTeam la repository dei membri del team
     * @param servizioNotifiche il servizio per le notifiche
     */
    public InvitaUtentiHandler(RepositoryUtenti repositoryUtenti, RepositoryMembriTeam repositoryMembriTeam, ServizioNotifiche servizioNotifiche){
        this.repositoryUtenti = repositoryUtenti;
        this.repositoryMembriTeam = repositoryMembriTeam;
        this.servizioNotifiche = servizioNotifiche;
    }

    //todo questo è da riguardare, sicuro ci va il leader nel boundary perchè è quello che inizia la chiamata http
    /**
     * Metodo per invitare utenti ad un team
     * @param nomeUtente il nome dell'utente da invitare
     * @param team il team a cui invitarlo
     */
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
