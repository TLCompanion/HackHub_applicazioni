package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.Hackathon;
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

    private final RepositoryTeam repositoryTeam;
    private final RepositoryUtenti repositoryUtenti;
    private final RepositoryMembriTeam repositoryMembriTeam;
    private final ServizioNotifiche servizioNotifiche;
    private final RepositoryIscrizioniTeam repositoryIscrizioniTeam;

    /**
     * Crea una nuova istanza dell'handler per gestire il team
     * @param repositoryTeam la repository dei team
     * @param repositoryUtenti la repository per gli utenti
     * @param repositoryMembriTeam la repository per i membri dell team
     * @param servizioNotifiche il servizio per le notifiche
     * @param repositoryIscrizioniTeam la repository per le iscrizioni dei team
     */
    public GestisciTeamHandler(RepositoryTeam repositoryTeam, RepositoryUtenti repositoryUtenti, RepositoryMembriTeam repositoryMembriTeam, ServizioNotifiche servizioNotifiche, RepositoryIscrizioniTeam repositoryIscrizioniTeam, RepositoryHackathon repositoryHackathon) {
        this.repositoryTeam = repositoryTeam;
        this.repositoryUtenti = repositoryUtenti;
        this.repositoryMembriTeam = repositoryMembriTeam;
        this.servizioNotifiche = servizioNotifiche;
        this.repositoryIscrizioniTeam = repositoryIscrizioniTeam;
    }

    /**
     * Metodo per cambiare nome ad un team
     * @param nomeUtente il leader che vuole cambiare nome
     * @param nome il nuovo nome del team
     */
    public void cambiaNomeTeam(String nomeUtente, String nome) {
        MembroTeam leader = repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente).orElseThrow(() -> new NotFoundException("L'utente non è membro di nessun team"));
        if (leader.getRuolo() != RuoloTeam.LEADER) {
            throw new ConflictException("L'utente non è il leader del team");
        }
        Team team = leader.getTeam();
        if (repositoryTeam.existsByNome(team.getNome())) {
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
     * @param idMembro l'id del membro che vuole uscire
     * @param idTeam l'id del team
     */
    public void esciDalTeam(String idMembro, String idTeam){
        if (repositoryMembriTeam.findByUtente_idUtente((idMembro)).isEmpty()) {
            throw new NotFoundException("L'utente non è membro di nessun team");
        }
//        if (repositoryTeam.existsById(idTeam)){
//            throw new NotFoundException("Il team non esiste");
//        }
        //todo, secondo me in questo caso non serve la stringa dell'id del team, possiamo semplicemente risalire al team dal membro del team così:
        MembroTeam membroTeam = repositoryMembriTeam.getMembroTeamById(idMembro).orElseThrow(() -> new NotFoundException("Membro del team non trovato"));
        Team team = membroTeam.getTeam();
        team.getMembri().remove(membroTeam);
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

        //controllo se è presente un iscrizione e in caso positivo la elimino
        List<IscrizioneTeam> iscrizioni = repositoryIscrizioniTeam.findAllByTeam(team);
        //todo qui sul sequence diagram c'è un loop per ogni iscrizione trova l'hackathon ma non conviene semplicemente prenderle ed eliminarle tutte?
        if (!iscrizioni.isEmpty()) {
            repositoryIscrizioniTeam.deleteAll(iscrizioni);
        }
        for(MembroTeam m : team.getMembri()){
            servizioNotifiche.creaNotifica(m.getUtente(), USCITA, "Il team " + team.getNome() + " è stato sciolto.");
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
        if (!repositoryIscrizioniTeam.findByTeam(team).isEmpty()) {
            throw new NotFoundException("Il team è iscritto ad un'hackathon, non puoi espellere un membro");
        }
        if (membroDaEspellere.getId().equals(leader.getId())) {
            throw new ConflictException("Il leader non può espellere se stesso");
        }
        team.getMembri().remove(membroDaEspellere);
        repositoryMembriTeam.delete(membroDaEspellere);
        repositoryTeam.save(team);
        for(MembroTeam m : team.getMembri()){
            servizioNotifiche.creaNotifica(m.getUtente(), ESPULSIONE_TEAM, "Il membro " + membroDaEspellere.getUtente().getNomeUtente() + " è stato espulso dal team.");
        }
    }
}