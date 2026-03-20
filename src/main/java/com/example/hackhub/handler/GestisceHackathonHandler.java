package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.*;
import com.example.hackhub.eccezioni.BadRequestException;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.*;
import com.example.hackhub.servizi.ServizioNotifiche;
import com.example.hackhub.servizi.esterni.SistemaDiPagamento;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.hackhub.domain.TipoNotifica.*;

@Service
public class GestisceHackathonHandler {

    private final ServizioNotifiche servizioNotifiche;
    private final RepositoryStaff repositoryStaff;
    private final RepositoryTeam repositoryTeam;
    private final RepositoryUtenti repositoryUtenti;
    private final RepositoryHackathon repositoryHackathon;
    private final RepositoryIscrizioniTeam repositoryIscrizioniTeam;
    private final SistemaDiPagamento sistemaDiPagamento;

    /**
     * Crea una nuova istanza di un handler per la gestione degli hackathon
     *
     * @param servizioNotifiche il servizio notifiche
     * @param repositoryStaff   la repository dello staff
     * @param repositoryTeam    la repository dei team
     */
    public GestisceHackathonHandler(ServizioNotifiche servizioNotifiche, RepositoryStaff repositoryStaff, RepositoryTeam repositoryTeam, RepositoryUtenti repositoryUtenti, RepositoryHackathon repositoryHackathon, RepositoryIscrizioniTeam repositoryIscrizioniTeam, SistemaDiPagamento sistemaDiPagamento) {
        this.servizioNotifiche = servizioNotifiche;
        this.repositoryStaff = repositoryStaff;
        this.repositoryTeam = repositoryTeam;
        this.repositoryUtenti = repositoryUtenti;
        this.repositoryHackathon = repositoryHackathon;
        this.repositoryIscrizioniTeam = repositoryIscrizioniTeam;
        this.sistemaDiPagamento = sistemaDiPagamento;
    }


    @Transactional
    public void segnalaViolazione(String nomeMentore, String nomeTeam, String nomeHackathon) {
        Staff mentore = validaAutorizzazione(nomeMentore, RuoloStaff.MENTORE);
        Team team = repositoryTeam.findByNome(nomeTeam).orElseThrow(() -> new NotFoundException("Team non trovato"));
        Hackathon hackathon = repositoryHackathon.findByNome(nomeHackathon).orElseThrow(() -> new NotFoundException("Hackathon non trovato"));
        checkStessoHackathon(hackathon, mentore);
        Staff organizzatore = hackathon.getStaff().stream().filter(s -> s.getRuolo() == RuoloStaff.ORGANIZZATORE).findFirst().orElseThrow(() -> new NotFoundException("Organizzatore non trovato"));
        IscrizioneTeam iscrizioneTeam = repositoryIscrizioniTeam.findByTeam(team).orElseThrow(() -> new NotFoundException("Il team non è iscritto a nessun hackathon"));
        if (!iscrizioneTeam.getHackathon().equals(mentore.getHackathon())) {
            throw new ConflictException("Il team non fa parte dello stesso hackathon del mentore");
        }
        if (!(hackathon.getStato() instanceof InCorso)) {
            throw new ConflictException("Non è possibile segnalare una violazione se l'hackathon non è in corso");
        }
        if (!mentore.getHackathon().equals(organizzatore.getHackathon())) {
            throw new ConflictException("Il mentore non fa parte dello stesso hackathon dell'organizzatore");
        }
        servizioNotifiche.creaNotifica(organizzatore.getUtente(), VIOLAZIONE_REGOLAMENTO,
                "Il team " + team.getNome() + " ha violato il regolamento dell'hackathon");
    }

    /**
     * Metodo per invitare un utente a diventare mentore di un hackathon. Solo gli organizzatori possono nominare mentori e le iscrizioni devono essere aperte
     *
     * @param nomeUtente           il nome dell'organizzatore
     * @param nomeUtenteDaInvitare il nome dell'utente da invitare
     * @param nomeHackathon il nome dell'hackathon
     */
    @Transactional
    public void nominaMentori(String nomeUtente, String nomeUtenteDaInvitare, String nomeHackathon) {
        Staff organizzatore = validaAutorizzazione(nomeUtente, RuoloStaff.ORGANIZZATORE);
        Utente staffDaInvitare = repositoryUtenti.findByNomeUtente(nomeUtenteDaInvitare)
                .orElseThrow(() -> new NotFoundException("Utente da invitare non trovato"));
        Hackathon hackathon = repositoryHackathon.findByNome(nomeHackathon).orElseThrow(() -> new NotFoundException("Hackathon non trovato"));
        checkStessoHackathon(hackathon, organizzatore);
        if (!(hackathon.getStato() instanceof IscrizioniAperte)) {
            throw new ConflictException("Non è possibile nominare mentori al di fuori della fase 'iscrizioni aperte'");
        }
        if (hackathon.getStaff().stream().anyMatch(s -> s.getUtente().equals(staffDaInvitare))) {
            throw new BadRequestException("L'utente da invitare è già nello staff");
        }
        servizioNotifiche.creaInvitoStaff(nomeUtente, staffDaInvitare, hackathon, RuoloStaff.MENTORE);
    }

    /**
     * Metodo per eliminare un hackathon se non è ancora iniziato
     *
     * @param nomeUtente    l'organizzatore
     * @param nomeHackathon il nome dell'hackathon
     */
    @Transactional
    public void eliminaHackathon(String nomeUtente, String nomeHackathon) {
        Staff organizzatore = validaAutorizzazione(nomeUtente, RuoloStaff.ORGANIZZATORE);
        Hackathon hackathon = repositoryHackathon.findByNome(nomeHackathon).orElseThrow(() -> new NotFoundException("Hackathon non trovato"));
        checkStessoHackathon(hackathon, organizzatore);
        if (hackathon.getStato() instanceof IscrizioniAperte || hackathon.getStato() instanceof IscrizioniChiuse) {
            throw new ConflictException("Non è possibile eliminare un hackathon in corso o concluso");
        }
        List<Team> teams = repositoryIscrizioniTeam.findAllByHackathon(hackathon).stream().map(IscrizioneTeam::getTeam).toList();
        for (Team t : teams) {
            for (MembroTeam m1 : t.getMembri()) {
                servizioNotifiche.creaNotifica(m1.getUtente(), HACKATHON_CANCELLATO, "L'hackathon a cui eri iscritto è stato cancellato");
            }
        }
        repositoryHackathon.delete(hackathon);
    }

    /**
     * Metodo per espellere un team
     *
     * @param nomeUtente    l'organizzatore
     * @param nomeHackathon il nome dell'hackathon
     * @param nomeTeam      il nome del team
     */
    @Transactional
    public void espelliTeam(String nomeUtente, String nomeHackathon, String nomeTeam) {
        Staff organizzatore = validaAutorizzazione(nomeUtente, RuoloStaff.ORGANIZZATORE);
        Hackathon hackathon = repositoryHackathon.findByNome(nomeHackathon).orElseThrow(() -> new NotFoundException("Hackathon non trovato"));
        checkStessoHackathon(hackathon, organizzatore);
        Team team = repositoryTeam.findByNome(nomeTeam)
                .orElseThrow(() -> new NotFoundException("Team non trovato"));
        if (repositoryIscrizioniTeam.findByTeamAndHackathon(team, hackathon).isEmpty()) {
            throw new NotFoundException("Iscrizione del team all'hackathon non trovata");
        }
        if (!(hackathon.getStato() instanceof InCorso)) {
            throw new ConflictException("Non è possibile espellere un team da un hackathon non ancora in corso");
        }
        hackathon.rimuoviIscrizione(team);
        repositoryHackathon.save(hackathon);
        for (MembroTeam m : team.getMembri()) {
            servizioNotifiche.creaNotifica(m.getUtente(), ESPULSIONE_TEAM, "Il tuo team è stato espulso dall'hackathon " + hackathon.getNome());
        }
    }

    /**
     * Proclama il vincitore di un hackathon
     *
     * @param nomeUtente    il nome utente dell'organizzatore
     * @param nomeHackathon il nome dell'hackathon
     * @param nomeTeam      il nome del team vincitore
     */
    @Transactional
    public void proclamaVincitore(String nomeUtente, String nomeHackathon, String nomeTeam) {
        Staff organizzatore = validaAutorizzazione(nomeUtente, RuoloStaff.ORGANIZZATORE);
        Hackathon hackathon = repositoryHackathon.findByNome(nomeHackathon).orElseThrow(() -> new NotFoundException("Hackathon non trovato"));
        checkStessoHackathon(hackathon, organizzatore);
        if (!(hackathon.getStato() instanceof Concluso)) {
            throw new ConflictException("Hackathon non concluso, impossibile proclamare il vincitore");
        }
        Team team = repositoryTeam.findByNome(nomeTeam).orElseThrow(() -> new NotFoundException("Team non trovato"));
        for (MembroTeam m : team.getMembri()) {
            servizioNotifiche.creaNotifica(m.getUtente(), VITTORIA, "Il tuo team ha vinto l'hackathon");
        }
        List<Team> teams = repositoryIscrizioniTeam.findAllByHackathon(hackathon).stream().map(IscrizioneTeam::getTeam).toList();
        for (Team t : teams) {
            if (!t.equals(team)) {
                for (MembroTeam m1 : t.getMembri()) {
                    servizioNotifiche.creaNotifica(m1.getUtente(), SCONFITTA, "Il tuo team non ha vinto l'hackathon");
                }
            }
        }
    }

    /**
     * Metodo per liquidare il premio
     *
     * @param nomeUtente    l'organizzatore
     * @param nomeHackathon il nome dell'hackathon
     * @param nomeTeam      il nome del team
     */
    @Transactional
    public void attivaLiquidazionePremio(String nomeUtente, String nomeHackathon, String nomeTeam) {
        Staff organizzatore = validaAutorizzazione(nomeUtente, RuoloStaff.ORGANIZZATORE);
        Hackathon hackathon = repositoryHackathon.findByNome(nomeHackathon).orElseThrow(() -> new NotFoundException("Hackathon non trovato"));
        checkStessoHackathon(hackathon, organizzatore);
        Team team = repositoryTeam.findByNome(nomeTeam).orElseThrow(() -> new NotFoundException("Team non trovato"));
        IscrizioneTeam iscrizione = repositoryIscrizioniTeam.findByTeamAndHackathon(team, hackathon).orElseThrow(() -> new NotFoundException("Iscrizione del team all'hackathon non trovata"));
        if (!(hackathon.getStato() instanceof Concluso)) {
            throw new ConflictException("Hackathon non concluso, impossibile liquidare il premio");
        }
        if (!organizzatore.getHackathon().equals(iscrizione.getHackathon())) {
            throw new ConflictException("Il team non è iscritto allo stesso hackathon dell'organizzatore");
        }
        for (MembroTeam m : team.getMembri()) {
            sistemaDiPagamento.pagaPremio(organizzatore.getUtente().getRecapitoBancario(), m.getUtente().getRecapitoBancario(), hackathon.getPremio());
        }
    }


    private Staff validaAutorizzazione(String nomeUtente, RuoloStaff ruoloStaff){
        Staff utente = repositoryStaff.findByUtente_NomeUtente(nomeUtente).orElseThrow(() -> new NotFoundException("L'utente non è un membro dello staff"));
        if (utente.getRuolo() != ruoloStaff) throw new ConflictException("L'utente non ha i permessi necessari per eseguire questa operazione");
        return utente;
    }

    private void checkStessoHackathon(Hackathon hackathon, Staff staff) {
        if (!staff.getHackathon().equals(hackathon)) {
            throw new ConflictException("Il membro dello staff non fa parte dello stesso hackathon");
        }
    }

}

