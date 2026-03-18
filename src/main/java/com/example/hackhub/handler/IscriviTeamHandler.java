package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.InCorso;
import com.example.hackhub.domain.implementazione.statePattern.IscrizioniChiuse;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.ForbiddenException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryIscrizioniTeam;
import com.example.hackhub.repository.RepositoryMembriTeam;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class IscriviTeamHandler {
    private final RepositoryMembriTeam repositoryMembriTeam;
    private final RepositoryHackathon repositoryHackathon;
    private final RepositoryIscrizioniTeam repositoryIscrizioniTeam;

    /**
     * Crea una nuova istanza dell'handler che gestisce l'iscrizione dei team
     * @param repositoryMembriTeam la repository dei membri del team
     * @param repositoryHackathon la repository degli hackathon
     * @param repositoryIscrizioniTeam la repository delle iscrizioni
     */
    public IscriviTeamHandler(RepositoryMembriTeam repositoryMembriTeam, RepositoryHackathon repositoryHackathon, RepositoryIscrizioniTeam repositoryIscrizioniTeam) {
        this.repositoryMembriTeam = repositoryMembriTeam;
        this.repositoryHackathon = repositoryHackathon;
        this.repositoryIscrizioniTeam = repositoryIscrizioniTeam;
    }

    /**
     * Avvia un iscrizione di un team ad un hackathon
     * @param nomeUtente il nome dell'utente che vuole iscrivere il team
     * @param nomeHackathon il nome dell'hackathon
     */
    @Transactional
    public void avviaIscrizioneHackathon(String nomeUtente, String nomeHackathon) {
        MembroTeam membroTeam = repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente).orElseThrow(() ->
                new NotFoundException("L'utente non è membro di nessun team"));

        if (!membroTeam.getRuolo().equals(RuoloTeam.LEADER))
            throw new ForbiddenException("L'utente non è il leader del team");

        Team team = membroTeam.getTeam();
        Hackathon hackathon = repositoryHackathon.findByNome(nomeHackathon).orElseThrow(() ->
                new NotFoundException("Hackathon non trovato"));
        checkIscrizioneInHackathon(hackathon, team);

        IscrizioneTeam iscrizione = new IscrizioneTeam(team, hackathon);
        hackathon.aggiungiIscrizione(iscrizione);
        repositoryHackathon.save(hackathon);
    }

    /**
     * Controlla che un team si possa iscrivere ad un hackathon
     * @param hackathon l'hackathon
     * @param team il team
     */
    private void checkIscrizioneInHackathon(Hackathon hackathon, Team team) {
        hackathon.getStato().verificaIscrizioneConsentita(hackathon);
        if (team.getNumMembri()<hackathon.getTeamMin() || team.getNumMembri()>hackathon.getTeamMax())
            throw new ConflictException("Il numero di membri del team non è compatibile con i requisiti dell'hackathon");

        IscrizioneTeam iscrizioneEsistente = repositoryIscrizioniTeam.findByTeamAndHackathon(team, hackathon).
                orElse(null);
        if (iscrizioneEsistente != null)
            if (iscrizioneEsistente.getHackathon().equals(hackathon))
                throw new ConflictException("Il team è già iscritto a questo hackathon");

        if (hackathon.getIscrizioni().size()>=hackathon.getMaxIscrizioni())
            throw new ConflictException("Il numero massimo di iscrizioni è già stato raggiunto");
    }

    public void annullaIscrizioneHackathon(String nomeUtente, String idHackathon){
        Hackathon hackathon = repositoryHackathon.findById(idHackathon).orElseThrow(() ->
                new NotFoundException("Hackathon non trovato"));

        if(hackathon.getStato() instanceof InCorso){
            throw new ConflictException("Non è possibile annullare l'iscrizione ad un hackathon in corso");
        }

        MembroTeam leader = repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente).orElseThrow(() ->
                new NotFoundException("L'utente non è membro di nessun team"));

        //todo again qui nel sequence diagram manca il check che è il leader, è da aggiungere?
        if (!leader.getRuolo().equals(RuoloTeam.LEADER)) {
            throw new ForbiddenException("L'utente non è il leader del team");
        }

        Team team = leader.getTeam();
        //todo non c'è neanche il getIscrizione o il controllo che l'iscrizione esista o meno
        IscrizioneTeam iscrizione = repositoryIscrizioniTeam.findByTeamAndHackathon(team, hackathon).
                orElseThrow(() -> new NotFoundException("Il team non è iscritto a questo hackathon"));

        //todo qui sul sequence diagram rimuove l'iscrizione dall'entità hackathon tramite il team però non so se si può fare senza prenderla dalla repository, per adesso ho messo così
        repositoryIscrizioniTeam.delete(iscrizione);
        repositoryHackathon.save(hackathon);
    }
}
