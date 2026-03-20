package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.MembroTeam;
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

    /**
     * Metodo per invitare utenti ad un team
     * @param nomeUtente il nome dell'utente da invitare
     * @param nomeUtenteDaInvitare l'utente da invitare
     */
    public void invitaUtenti(String nomeUtente, String nomeUtenteDaInvitare) {
        MembroTeam leader = repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente).orElseThrow(() -> new NotFoundException("membro non presente nel team"));
        if (leader.getRuolo()!= RuoloTeam.LEADER) {
            throw new ConflictException("Solo il leader può invitare utenti");
        }
        Utente utente = repositoryUtenti.findByNomeUtente(nomeUtenteDaInvitare)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
        if (repositoryMembriTeam.findByUtente_NomeUtente(utente.getNomeUtente()).isPresent()) {
            throw new ConflictException("L'utente appartiene già a un team");
        }
        Team team = leader.getTeam();
        servizioNotifiche.creaInvitoTeam(leader.getUtente().getNomeUtente(), utente, team);
    }
}
