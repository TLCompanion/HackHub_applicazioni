package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.Concluso;
import com.example.hackhub.domain.implementazione.statePattern.IscrizioniAperte;
import com.example.hackhub.domain.implementazione.statePattern.StatoHackathon;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryIscrizioniTeam;
import com.example.hackhub.repository.RepositoryStaff;
import com.example.hackhub.repository.RepositoryTeam;
import com.example.hackhub.servizi.ServizioNotifiche;
import com.example.hackhub.servizi.esterni.SistemaDiPagamento;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.hackhub.domain.TipoNotifica.SCONFITTA;
import static com.example.hackhub.domain.TipoNotifica.VITTORIA;

@Service
public class ProclamaVincitoreHandler {

    private final RepositoryHackathon repositoryHackathon;
    private final RepositoryStaff repositoryStaff;
    private final RepositoryTeam repositoryTeam;
    private final ServizioNotifiche servizioNotifiche;
    private final RepositoryIscrizioniTeam repositoryIscrizioniTeam;
    private final SistemaDiPagamento sistemaDiPagamento;

    public ProclamaVincitoreHandler(RepositoryHackathon repositoryHackathon, RepositoryStaff repositoryStaff, RepositoryTeam repositoryTeam, ServizioNotifiche servizioNotifiche, RepositoryIscrizioniTeam repositoryIscrizioniTeam, SistemaDiPagamento sistemaDiPagamento) {
        this.repositoryHackathon = repositoryHackathon;
        this.repositoryStaff = repositoryStaff;
        this.repositoryTeam = repositoryTeam;
        this.servizioNotifiche = servizioNotifiche;
        this.repositoryIscrizioniTeam = repositoryIscrizioniTeam;
        this.sistemaDiPagamento = sistemaDiPagamento;
    }

    /**
     * Proclama il vincitore di un hackathon
     * @param nomeUtente il nome utente dell'organizzatore
     * @param nomeHackathon il nome dell'hackathon
     * @param nomeTeam il nome del team vincitore
     */
    public void proclamaVincitore(String nomeUtente, String nomeHackathon, String nomeTeam) {
        Hackathon hackathon = repositoryHackathon.findByNomeHackathon(nomeHackathon).orElseThrow(() -> new RuntimeException("Hackathon non trovato"));
        Staff organizzatore = repositoryStaff.findByUtente_NomeUtente(nomeUtente).orElseThrow(() -> new RuntimeException("Organizzatore non trovato"));
        if (organizzatore.getRuolo() != RuoloStaff.ORGANIZZATORE){
            throw new ConflictException("Utente non autorizzato a proclamare il vincitore perchè non è l'organizzatore");
        }
        if (!organizzatore.getHackathon().equals(hackathon)){
            throw new ConflictException("Utente non autorizzato a proclamare il vincitore perchè non è l'organizzatore di questo hackathon");
        }
        if (hackathon.getStato() instanceof Concluso){
            throw new ConflictException("Hackathon non concluso, impossibile proclamare il vincitore");
        }
        Team team = repositoryTeam.findByNomeTeam(nomeTeam).orElseThrow(() -> new NotFoundException("Team non trovato"));
        for(MembroTeam m : team.getMembri()){
            servizioNotifiche.creaNotifica(m.getUtente(), VITTORIA,"Il tuo team ha vinto l'hackathon");
        }
        List<Team> teams = repositoryIscrizioniTeam.findAllByHackathon(hackathon).stream().map(IscrizioneTeam::getTeam).toList();
        for (Team t : teams){
            if (!t.equals(team)){
                for (MembroTeam m1 : t.getMembri()){
                    servizioNotifiche.creaNotifica(m1.getUtente(), SCONFITTA ,"Il tuo team non ha vinto l'hackathon");
                }
            }
        }
    }

    public void attivaLiquidazionePremio(String nomeUtente, String nomeHackathon, String nomeTeam){
        Staff organizzatore = repositoryStaff.findByUtente_NomeUtente(nomeUtente).orElseThrow(() -> new NotFoundException("Organizzatore non trovato"));
        Hackathon hackathon = repositoryHackathon.findByNomeHackathon(nomeHackathon).orElseThrow(() -> new NotFoundException("Hackathon non trovato"));
        Team team = repositoryTeam.findByNomeTeam(nomeTeam).orElseThrow(() -> new NotFoundException("Team non trovato"));
        if (organizzatore.getRuolo() != RuoloStaff.ORGANIZZATORE){
            throw new ConflictException("Utente non autorizzato a liquidare il premio perchè non è l'organizzatore");
        }
        if (!organizzatore.getHackathon().equals(hackathon)){
            throw new ConflictException("Utente non autorizzato a liquidare il premio perchè non è l'organizzatore di questo hackathon");
        }
        IscrizioneTeam iscrizione = repositoryIscrizioniTeam.findByTeamAndHackathon(team, hackathon).orElseThrow(() -> new NotFoundException("Iscrizione del team all'hackathon non trovata"));
        if (!(hackathon.getStato() instanceof Concluso)) {
            throw new ConflictException("Hackathon non concluso, impossibile liquidare il premio");
        }
        if (!organizzatore.getHackathon().equals(hackathon)){
            throw new ConflictException("L'organizzatore non è di questo hackathon");
        }
        if (!organizzatore.getHackathon().equals(iscrizione.getHackathon())){
            throw new ConflictException("Il team non è iscritto allo stesso hackathon dell'organizzatore");
        }
        for (MembroTeam m: team.getMembri()){
        sistemaDiPagamento.pagaPremio(organizzatore.getUtente().getRecapitoBancario(), m.getUtente().getRecapitoBancario() , hackathon.getPremio());
        }
    }
}
