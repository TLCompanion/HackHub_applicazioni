package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.Concluso;
import com.example.hackhub.domain.implementazione.statePattern.InCorso;
import com.example.hackhub.domain.implementazione.statePattern.IscrizioniAperte;
import com.example.hackhub.eccezioni.BadRequestException;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.*;
import com.example.hackhub.servizi.ServizioNotifiche;
import com.example.hackhub.servizi.esterni.SistemaDiPagamento;
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
     * @param servizioNotifiche il servizio notifiche
     * @param repositoryStaff la repository dello staff
     * @param repositoryTeam la repository dei team
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

    /**
     * Segnala che un team ha violato il regolamento
     * @param nomeOrganizzatore l'organizzatore da notificare
     * @param nomeTeam il nome del team che ha violato il regolamento
     */
    public void segnalaViolazione(String nomeOrganizzatore, String nomeMentore, String nomeTeam){
        Team team = repositoryTeam.findByNomeTeam(nomeTeam).orElseThrow(() -> new NotFoundException("Team non trovato"));
        Staff mentore = repositoryStaff.findByUtente_NomeUtente(nomeMentore)
                .orElseThrow(() -> new NotFoundException("Mentore non trovato"));
        Staff organizzatore = repositoryStaff.findByUtente_NomeUtente(nomeOrganizzatore)
                .orElseThrow(() -> new NotFoundException("Organizzatore non trovato"));
        IscrizioneTeam iscrizioneTeam = repositoryIscrizioniTeam.findByTeam(team).orElseThrow(() -> new NotFoundException("Il team non è iscritto a nessun hackathon"));
        if (!iscrizioneTeam.getHackathon().equals(organizzatore.getHackathon())){
            throw new ConflictException("Il team non fa parte dello stesso hackathon dell'organizzatore");
        }
        if (mentore.getRuolo() != RuoloStaff.MENTORE) {
            throw new ConflictException("Solo i mentori possono segnalare una violazione");
        }
        if (!(organizzatore.getHackathon().getStato() instanceof InCorso)) {
            throw new ConflictException("Non è possibile segnalare una violazione se l'hackathon non è in corso");
        }
        if (!mentore.getHackathon().equals(organizzatore.getHackathon())){
            throw new ConflictException("Il mentore non fa parte dello stesso hackathon dell'organizzatore");
        }
        servizioNotifiche.creaNotifica(organizzatore.getUtente(), VIOLAZIONE_REGOLAMENTO,
                "Il team " + team.getNome() + " ha violato il regolamento dell'hackathon");
    }

    /**
     * Metodo per invitare un utente a diventare mentore di un hackathon. Solo gli organizzatori possono nominare mentori e le iscrizioni devono essere aperte
     * @param nomeUtente il nome dell'organizzatore
     * @param nomeUtenteDaInvitare il nome dell'utente da invitare
     */
    public void nominaMentori(String nomeUtente, String nomeUtenteDaInvitare){
        Staff organizzatore = repositoryStaff.findByUtente_NomeUtente(nomeUtente)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
        if (organizzatore.getRuolo() != RuoloStaff.ORGANIZZATORE) {
            throw new ConflictException("Solo gli organizzatori possono nominare mentori");
        }
        Utente staffDaInvitare = repositoryUtenti.findByNomeUtente(nomeUtenteDaInvitare)
                .orElseThrow(() -> new NotFoundException("Utente da invitare non trovato"));
        Hackathon hackathon = organizzatore.getHackathon();
        if(!(hackathon.getStato() instanceof IscrizioniAperte)) {
            throw new ConflictException("Non è possibile nominare mentori se le iscrizioni non sono aperte");
        }
        if (repositoryStaff.findByUtente_NomeUtente(nomeUtenteDaInvitare).isPresent()) {
            throw new BadRequestException("L'utente da invitare è già nello staff");
        }
        servizioNotifiche.creaInvitoStaff(nomeUtente, staffDaInvitare, hackathon, RuoloStaff.MENTORE);
    }

    /**
     * Metodo per eliminare un hackathon se non è ancora iniziato
     * @param nomeUtente l'organizzatore
     * @param idHackathon l'id dell'hackathon
     */
    public void eliminaHackathon(String nomeUtente, String idHackathon) {
        Staff organizzatore = repositoryStaff.findByUtente_NomeUtente(nomeUtente)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
        Hackathon hackathon = repositoryHackathon.findByIdHackathon(idHackathon)
                .orElseThrow(() -> new NotFoundException("Hackathon non trovato"));
        if (organizzatore.getRuolo() != RuoloStaff.ORGANIZZATORE) {
            throw new ConflictException("Solo gli organizzatori possono eliminare un hackathon");
        }
        if (hackathon.getStato() instanceof InCorso) {
            throw new ConflictException("Non è possibile eliminare un hackathon in corso");
        }
        if (!organizzatore.getHackathon().equals(hackathon)) {
            throw new ConflictException("L'organizzatore non può eliminare un hackathon di cui non fa parte");
        }
        List<Team> teams = repositoryIscrizioniTeam.findAllByHackathon(hackathon).stream().map(IscrizioneTeam::getTeam).toList();
        for (Team t : teams){
                for (MembroTeam m1 : t.getMembri()){
                    servizioNotifiche.creaNotifica(m1.getUtente(), HACKATHON_CANCELLATO ,"L'hackathon a cui eri iscritto è stato cancellato");
            }
        }
        repositoryHackathon.delete(hackathon);
    }

    /**
     * Metodo per espellere un team
     * @param nomeUtente l'organizzatore
     * @param nomeHackathon il nome dell'hackathon
     * @param nomeTeam il nome del team
     */
    public void espelliTeam(String nomeUtente, String nomeHackathon, String nomeTeam) {
        Staff organizzatore = repositoryStaff.findByUtente_NomeUtente(nomeUtente)
                .orElseThrow(() -> new NotFoundException("Organizzatore non trovato"));
        Hackathon hackathon = repositoryHackathon.findByNomeHackathon(nomeHackathon)
                .orElseThrow(() -> new NotFoundException("Hackathon non trovato"));
        Team team = repositoryTeam.findByNomeTeam(nomeTeam)
                .orElseThrow(() -> new NotFoundException("Team non trovato"));
        IscrizioneTeam iscrizione = repositoryIscrizioniTeam.findByTeamAndHackathon(team, hackathon)
                .orElseThrow(() -> new NotFoundException("Iscrizione del team all'hackathon non trovata"));
        if (organizzatore.getRuolo() != RuoloStaff.ORGANIZZATORE) {
            throw new ConflictException("Solo gli organizzatori possono espellere un team");
        }
        if (!(hackathon.getStato() instanceof InCorso)) {
            throw new ConflictException("Non è possibile espellere un team da un hackathon non ancora in corso");
        }
        if (!organizzatore.getHackathon().equals(iscrizione.getHackathon())) {
            throw new ConflictException("L'organizzatore non può espellere un team da un hackathon di cui non fa parte");
        }
        hackathon.rimuoviIscrizione(team);
        repositoryHackathon.save(hackathon);
        for (MembroTeam m : team.getMembri()) {
            servizioNotifiche.creaNotifica(m.getUtente(), ESPULSIONE_TEAM, "Il tuo team è stato espulso dall'hackathon " + hackathon.getNome());
        }
    }

        /**
         * Proclama il vincitore di un hackathon
         * @param nomeUtente il nome utente dell'organizzatore
         * @param nomeHackathon il nome dell'hackathon
         * @param nomeTeam il nome del team vincitore
         */
        public void proclamaVincitore(String nomeUtente, String nomeHackathon, String nomeTeam) {
            Hackathon hackathon = repositoryHackathon.findByNomeHackathon(nomeHackathon).orElseThrow(() -> new NotFoundException("Hackathon non trovato"));
            Staff organizzatore = repositoryStaff.findByUtente_NomeUtente(nomeUtente).orElseThrow(() -> new NotFoundException("Organizzatore non trovato"));
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

    /**
     * Metodo per liquidare il premio
     * @param nomeUtente l'organizzatore
     * @param nomeHackathon il nome dell'hackathon
     * @param nomeTeam il nome del team
     */
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
