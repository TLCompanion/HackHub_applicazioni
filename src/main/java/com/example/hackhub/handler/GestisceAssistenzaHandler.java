package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.domain.implementazione.MembroTeam;
import com.example.hackhub.domain.implementazione.Staff;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryIscrizioniTeam;
import com.example.hackhub.repository.RepositoryMembriTeam;
import com.example.hackhub.repository.RepositoryStaff;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;

import static com.example.hackhub.domain.TipoNotifica.ASSISTENZA;

@Service
public class GestisceAssistenzaHandler {

    private final RepositoryMembriTeam repositoryMembriTeam;
    private final RepositoryIscrizioniTeam repositoryIscrizioniTeam;
    private final RepositoryHackathon repositoryHackathon;
    private final RepositoryStaff repositoryStaff;
    private final ServizioNotifiche servizioNotifiche;

    /**
     * Crea un istanza dell'handler
     * @param repositoryMembriTeam la repository dei membri de team
     * @param repositoryIscrizioniTeam la repository delle iscrizioni
     * @param repositoryHackathon la repository degli hackathon
     * @param repositoryStaff la repository dello staff
     * @param servizioNotifiche il servizio per le notifiche
     */
    public GestisceAssistenzaHandler(RepositoryMembriTeam repositoryMembriTeam, RepositoryIscrizioniTeam repositoryIscrizioniTeam, RepositoryHackathon repositoryHackathon, RepositoryStaff repositoryStaff, ServizioNotifiche servizioNotifiche) {
        this.repositoryMembriTeam = repositoryMembriTeam;
        this.repositoryIscrizioniTeam = repositoryIscrizioniTeam;
        this.repositoryHackathon = repositoryHackathon;
        this.repositoryStaff = repositoryStaff;
        this.servizioNotifiche = servizioNotifiche;
    }

    /**
     * Permette al leader del team di richiedere assistenza ad un mentore associato all'hackathon a cui il team è iscritto
     * @param nomeUtente del leader del team che richiede assistenza
     * @param idMentore il mentore a cui si vuole chiedere assistenza
     * @param idHackathon l'hackathon associato
     */
    public void chiediAssistenza(String nomeUtente, String idMentore, String idHackathon){
        MembroTeam leader = repositoryMembriTeam.findByUtente_NomeUtente(nomeUtente).orElseThrow(() -> new IllegalArgumentException("L'utente non è membro di alcun team."));
        if (leader.getRuolo() != RuoloTeam.LEADER){
            throw new IllegalArgumentException("Solo il leader del team può richiedere assistenza.");
        }
        Hackathon hackathon = repositoryHackathon.findById(idHackathon).orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato."));
        if (repositoryIscrizioniTeam.findByTeamAndHackathon(leader.getTeam(), hackathon).isEmpty()){
            throw new IllegalArgumentException("Il team non è iscritto all'hackathon.");
        }
        Staff mentore = repositoryStaff.getStaffById(idMentore).stream().filter(s -> s.getRuolo() == RuoloStaff.MENTORE).findFirst().orElseThrow(() -> new NotFoundException("L'utente selezionato non è un mentore"));
        if (hackathon.equals(mentore.getHackathon())){
            throw new IllegalArgumentException("Il mentore selezionato non è associato all'hackathon.");
        }
        servizioNotifiche.creaNotifica(mentore.getUtente(), ASSISTENZA, "Richiesta di assistenza");
    }
}
