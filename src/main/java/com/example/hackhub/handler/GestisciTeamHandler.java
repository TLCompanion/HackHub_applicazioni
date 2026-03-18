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

import static com.example.hackhub.domain.TipoNotifica.ESPULSIONE_TEAM;
import static com.example.hackhub.domain.TipoNotifica.USCITA;

@Service
public class GestisciTeamHandler {

    private final RepositoryTeam repositoryTeam;
    private final RepositoryUtenti repositoryUtenti;
    private final RepositoryMembriTeam repositoryMembriTeam;
    private final ServizioNotifiche servizioNotifiche;
    private final RepositoryIscrizioniTeam repositoryIscrizioniTeam;
    private final RepositoryHackathon repositoryHackathon;

    public GestisciTeamHandler(RepositoryTeam repositoryTeam, RepositoryUtenti repositoryUtenti, RepositoryMembriTeam repositoryMembriTeam, ServizioNotifiche servizioNotifiche, RepositoryIscrizioniTeam repositoryIscrizioniTeam, RepositoryHackathon repositoryHackathon) {
        this.repositoryTeam = repositoryTeam;
        this.repositoryUtenti = repositoryUtenti;
        this.repositoryMembriTeam = repositoryMembriTeam;
        this.servizioNotifiche = servizioNotifiche;
        this.repositoryIscrizioniTeam = repositoryIscrizioniTeam;
        this.repositoryHackathon = repositoryHackathon;
    }

    public void cambiaNome(String nomeUtente, String nome, Team team) {
        //todo sul sequence manca di controllare che l'utente sia il leader del team, altrimenti non può cambiare il nome del team
        if (repositoryUtenti.findByNomeUtente(nomeUtente).isEmpty()) {
            throw new NotFoundException("L'utente non esiste");
        }
        if (repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente).stream().filter(m -> m.getRuolo() == RuoloTeam.LEADER).findFirst().isEmpty()) {
            throw new ConflictException("L'utente non è il leader del team");
        }
        if (repositoryTeam.existsById(team.getIdTeam())) {
            throw new NotFoundException("Il team non esiste");
        }
        if (repositoryTeam.existsByNome(team.getNome())) {
            throw new ConflictException("Esiste già un team con questo nome");
        }
        team.setNome(nome);
        repositoryTeam.save(team);
    }

    public void esciDalTeam(String idMembro, String idTeam){
        if (repositoryMembriTeam.findByUtente_idUtente((idMembro)).isEmpty()) {
            throw new NotFoundException("L'utente non è membro di nessun team");
        }
//        if (repositoryTeam.existsById(idTeam)){
//            throw new NotFoundException("Il team non esiste");
//        }
        //todo, secondo me in questo caso non serve la stringa dell'id del team, possiamo semplicemente risalire al team dal membro del team così:
        MembroTeam membroTeam = repositoryMembriTeam.getMembroTeamById(idMembro);
        Team team = membroTeam.getTeam();
        team.getMembri().remove(membroTeam);
        repositoryMembriTeam.delete(membroTeam);
        repositoryTeam.save(team);
        for(MembroTeam m : team.getMembri()){
            servizioNotifiche.creaNotifica(m.getUtente(), USCITA, "Il membro " + membroTeam.getUtente().getNomeUtente() + " è uscito dal team.");
        }
    }

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

    public void espelliMembro(String nomeUtente, String idMembro){
        //todo sul sequence diagram non c'è il nome utente, ma pensavo che visto che è il leader che decidere di espellere un membro forse ci serve e ci servono anche qeusti controlli?
        MembroTeam leader = repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente).orElseThrow(() -> new NotFoundException("L'utente non è membro di nessun team"));
        if (leader.getRuolo() != RuoloTeam.LEADER) {
            throw new ConflictException("L'utente non è il leader del team");
        }
        //todo qui ho usato un metodo diverso della repository forse è sbagliato
        MembroTeam membroDaEspellere = repositoryMembriTeam.getMembroTeamById(idMembro);
        if (!membroDaEspellere.getTeam().equals(leader.getTeam())) {
            throw new ConflictException("Il membro da espellere non è nel team del leader");
        }
        Team team = leader.getTeam();
        team.getMembri().remove(membroDaEspellere);
        //todo su uml manca da aggiornare il team
        repositoryMembriTeam.delete(membroDaEspellere);
        repositoryTeam.save(team);
        for(MembroTeam m : team.getMembri()){
            servizioNotifiche.creaNotifica(m.getUtente(), ESPULSIONE_TEAM, "Il membro " + membroDaEspellere.getUtente().getNomeUtente() + " è stato espulso dal team.");
        }
    }
}