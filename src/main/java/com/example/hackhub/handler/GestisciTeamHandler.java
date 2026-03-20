package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.IscrizioneTeam;
import com.example.hackhub.domain.implementazione.MembroTeam;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.*;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.hackhub.domain.TipoNotifica.*;

@Service
public class GestisciTeamHandler {

    //TODO togliere sia qui che nel uml le repo inutilizzate (se nel sequence sono utilizzate capire perchè alla fine
    // non sono servite e aggiornare anche quello)
    private final RepositoryTeam repositoryTeam;
    private final RepositoryUtenti repositoryUtenti;
    private final RepositoryMembriTeam repositoryMembriTeam;
    private final ServizioNotifiche servizioNotifiche;
    private final RepositoryIscrizioniTeam repositoryIscrizioniTeam;
    private final GestisciRichiesteHandler gestisciRichiesteHandler;

    /**
     * Crea una nuova istanza dell'handler per gestire il team
     * @param repositoryTeam la repository dei team
     * @param repositoryUtenti la repository per gli utenti
     * @param repositoryMembriTeam la repository per i membri dell team
     * @param servizioNotifiche il servizio per le notifiche
     * @param repositoryIscrizioniTeam la repository per le iscrizioni dei team
     */
    public GestisciTeamHandler(RepositoryTeam repositoryTeam, RepositoryUtenti repositoryUtenti, RepositoryMembriTeam repositoryMembriTeam, ServizioNotifiche servizioNotifiche, RepositoryIscrizioniTeam repositoryIscrizioniTeam, RepositoryHackathon repositoryHackathon, GestisciRichiesteHandler gestisciRichiesteHandler) {
        this.repositoryTeam = repositoryTeam;
        this.repositoryUtenti = repositoryUtenti;
        this.repositoryMembriTeam = repositoryMembriTeam;
        this.servizioNotifiche = servizioNotifiche;
        this.repositoryIscrizioniTeam = repositoryIscrizioniTeam;
        this.gestisciRichiesteHandler = gestisciRichiesteHandler;
    }

    /**
     * Metodo per cambiare nome ad un team
     *
     * @param nomeUtente il leader che vuole cambiare nome
     * @param nome       il nuovo nome del team
     */
    public void cambiaNomeTeam(String nomeUtente, String nome) {
        MembroTeam leader = repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente).orElseThrow(() -> new NotFoundException("L'utente non è membro di nessun team"));
        if (leader.getRuolo() != RuoloTeam.LEADER) {
            throw new ConflictException("L'utente non è il leader del team");
        }
        Team team = leader.getTeam();
        if (repositoryTeam.existsByNome(nome)) {
            throw new ConflictException("Esiste già un team con questo nome");
        }
        team.setNome(nome);
        repositoryTeam.save(team);
        for (MembroTeam membro : team.getMembri()){
            servizioNotifiche.creaNotifica(membro.getUtente(), CAMBIO_NOME_TEAM, "Il team ha cambiato nome in " + nome + ".");
        }
    }

    /**
     * Metodo per uscire da un team
     *
     * @param nomeUtente l'id del membro che vuole uscire
     */
    public void esciDalTeam(String nomeUtente){
        MembroTeam membroTeam = repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente).orElseThrow(() -> new NotFoundException("L'utente non è membro di nessun team"));
        Team team = repositoryTeam.findByNome(membroTeam.getTeam().getNome()).orElseThrow(() -> new NotFoundException("Il team non esiste"));
        if (!membroTeam.getTeam().equals(team)) {
            throw new ConflictException("L'utente non è membro di questo team");
        }
        if (repositoryIscrizioniTeam.findByTeam(team).isPresent()){
            throw new ConflictException("Il team è iscritto ad un'hackathon, non puoi uscire dal team");
        }
        if (membroTeam.getRuolo() == RuoloTeam.LEADER) {
            if (team.getNumMembri() != 1)
                throw new ConflictException("Prima di uscire dal team è necessario nominare un nuovo leader");
        }
        team.rimuoviMembro(membroTeam);
        repositoryMembriTeam.delete(membroTeam);
        repositoryTeam.save(team);
        for(MembroTeam m : team.getMembri()){
            servizioNotifiche.creaNotifica(m.getUtente(), USCITA, "Il membro " + membroTeam.getUtente().getNomeUtente() + " è uscito dal team.");
        }
    }

    /**
     * Metodo per sciogliere un team
     * @param nomeUtente il nome dell'utente che vuole sciogliere il team
     */
    public void sciogliTeam(String nomeUtente){
        MembroTeam membroTeam = repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente).orElseThrow(() -> new NotFoundException("L'utente non è membro di nessun team"));
        if (membroTeam.getRuolo() != RuoloTeam.LEADER) {
            throw new ConflictException("L'utente non è il leader del team");
        }
        Team team = membroTeam.getTeam();
        List<IscrizioneTeam> iscrizioni = repositoryIscrizioniTeam.findAllByTeam(team);
        if (!iscrizioni.isEmpty()) {
            repositoryIscrizioniTeam.deleteAll(iscrizioni);
        }
        for(MembroTeam m : team.getMembri()){
            servizioNotifiche.creaNotifica(m.getUtente(), SCIOGLIMENTO_TEAM, "Il team " + team.getNome() + " è stato sciolto.");
        }
        repositoryTeam.delete(team);
    }

    /**
     * Metodo per espellere un membro da un team
     * @param nomeUtente il nome dell'utente che vuole espellere il membro
     * @param nomeMembro il nome del membro da espellere
     */
    public void espelliMembro(String nomeUtente, String nomeMembro){
        MembroTeam leader = repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente).orElseThrow(() -> new NotFoundException("L'utente non è membro di nessun team"));
        if (leader.getRuolo() != RuoloTeam.LEADER) {
            throw new ConflictException("L'utente non è il leader del team");
        }
        MembroTeam membroDaEspellere = repositoryMembriTeam.findByUtente_NomeUtente(nomeMembro).orElseThrow(() -> new NotFoundException("Il membro da espellere non esiste"));
        if (!membroDaEspellere.getTeam().equals(leader.getTeam())) {
            throw new ConflictException("Il membro da espellere non è nel team del leader");
        }
        Team team = leader.getTeam();
        if (repositoryIscrizioniTeam.findByTeam(team).isPresent()) {
            throw new NotFoundException("Il team è iscritto ad un'hackathon, non puoi espellere un membro");
        }
        if (membroDaEspellere.getIdMembroTeam().equals(leader.getIdMembroTeam())) {
            throw new ConflictException("Il leader non può espellere se stesso");
        }
        team.getMembri().remove(membroDaEspellere);
        repositoryMembriTeam.delete(membroDaEspellere);
        repositoryTeam.save(team);
        for(MembroTeam m : team.getMembri()){
            servizioNotifiche.creaNotifica(m.getUtente(), ESPULSIONE_TEAM, "Il membro " + membroDaEspellere.getUtente().getNomeUtente() + " è stato espulso dal team.");
        }
    }

    public void trasferisceRuoloLeader(String nomeUtente, String nomeMembro) {
        MembroTeam leader = repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente).orElseThrow(() -> new NotFoundException("L'utente non è membro di nessun team"));
        if (leader.getRuolo() != RuoloTeam.LEADER) {
            throw new ConflictException("L'utente non è il leader del team");
        }
        MembroTeam membroTeam = repositoryMembriTeam.findByUtente_NomeUtente(nomeMembro).orElseThrow(() -> new NotFoundException("Il membro da nominare non esiste"));
        if (!membroTeam.getTeam().equals(leader.getTeam())) {
            throw new ConflictException("Il membro da nominare non è nel team del leader");
        }
        if (membroTeam.getRuolo() == RuoloTeam.LEADER) {
            throw new ConflictException("Il membro da nominare è già il leader del team");
        }
        servizioNotifiche.creaPropostaLeader(nomeUtente, membroTeam.getUtente(), leader.getTeam());
    }
}