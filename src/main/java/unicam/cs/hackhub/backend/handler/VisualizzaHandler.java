package unicam.cs.hackhub.backend.handler;

import jakarta.transaction.Transactional;
import unicam.cs.hackhub.backend.boundary.dto.*;
import unicam.cs.hackhub.backend.domain.implementazione.*;
import unicam.cs.hackhub.backend.eccezioni.ConflictException;
import unicam.cs.hackhub.backend.eccezioni.NotFoundException;
import unicam.cs.hackhub.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VisualizzaHandler {

    private final RepositoryHackathon repositoryHackathon;
    private final RepositoryRichiesta repositoryRichiesta;
    private final RepositoryNotifica repositoryNotifica;
    private final RepositoryUtente repositoryUtente;
    private final RepositoryStaff repositoryStaff;
    private final RepositoryMembriTeam repositoryMembriTeam;
    private final RepositoryTeam repositoryTeam;

    /**
     * Costruttore che inizializza questo handler per visualizzare liste di oggetti
     *
     * @param repositoryHackathon la repository degli hackathon
     */
    public VisualizzaHandler(RepositoryHackathon repositoryHackathon, RepositoryRichiesta repositoryRichiesta, RepositoryNotifica repositoryNotifica, RepositoryUtente repositoryUtente, RepositoryStaff repositoryStaff, RepositoryMembriTeam repositoryMembriTeam, RepositoryTeam repositoryTeam) {
        this.repositoryHackathon = repositoryHackathon;
        this.repositoryRichiesta = repositoryRichiesta;
        this.repositoryNotifica = repositoryNotifica;
        this.repositoryUtente = repositoryUtente;
        this.repositoryStaff = repositoryStaff;
        this.repositoryMembriTeam = repositoryMembriTeam;
        this.repositoryTeam = repositoryTeam;
    }

    private Hackathon validaAutorizzazioni(String nomeUtente, String nomeHackathon) {
        verificaUtenteOrFail(nomeUtente);
        Staff staff = repositoryStaff.findByUtente_NomeUtente(nomeUtente)
                .orElseThrow(() -> new ConflictException("L'utente non è membro di nessuno staff"));
        if (!staff.getHackathon().getNome().equals(nomeHackathon)) {
            throw new ConflictException("L'utente non è membro dello staff di questo hackathon");
        }
        return repositoryHackathon.findByNome(nomeHackathon)
                .orElseThrow(() -> new NotFoundException("Hackathon non trovato"));
    }

    private Utente verificaUtenteOrFail(String nomeUtente) {
        return repositoryUtente.findByNomeUtente(nomeUtente).orElseThrow(() ->
                new NotFoundException("Utente non trovato"));
    }

    /**
     * Metodo che ritorna la lista di valutazioni delle sottomissioni consegnate a un hackathon
     *
     * @param nomeHackathon il nome dell'hackathon di riferimento
     * @return la lista delle valutazioni
     */
    @Transactional
    public List<ValutazioneRequest> viewValutazioni(String nomeUtente, String nomeHackathon) {
        Hackathon hackathon = validaAutorizzazioni(nomeUtente, nomeHackathon);
        List<Valutazione> valutazioni = new ArrayList<>();
        for (IscrizioneTeam i : hackathon.getIscrizioni()) {
            valutazioni.add(i.getSottomissione().getValutazione());
        }
        return valutazioni.stream().map(v -> new ValutazioneRequest(v.getDescrizione(), v.getVoto())).collect(Collectors.toList());
    }

    /**
     * Metodo che ritorna la lista di sottomissioni consegnate in un hackathon
     *
     * @param nomeHackathon il nome dell'hackathon di riferimento
     * @return la lista di sottomissioni
     */
    @Transactional
    public List<SottomissioneDTO> viewSottomissioni(String nomeUtente, String nomeHackathon) {
        Hackathon hackathon = validaAutorizzazioni(nomeUtente, nomeHackathon);
        List<Sottomissione> sottomissioni = new ArrayList<>();
        for (IscrizioneTeam i : hackathon.getIscrizioni()) {
            sottomissioni.add(i.getSottomissione());
        }

        return sottomissioni.stream().map(s -> new SottomissioneDTO
                (s.getLink(), s.getValutazione().getDescrizione(), s.getValutazione().getVoto())).collect(Collectors.toList());
    }

    /**
     * Metodo che ritorna la lista di iscrizioni effettuate a un hackathon
     *
     * @param nomeHackathon il nome dell'hackathon di riferimento
     * @return la lista delle iscrizioni
     */
    @Transactional
    public List<IscrizioneTeamDTO> viewIscrizioni(String nomeUtente, String nomeHackathon) {
        Hackathon hackathon = validaAutorizzazioni(nomeUtente, nomeHackathon);
        return hackathon.getIscrizioni().stream().map(i -> new IscrizioneTeamDTO
                (i.getHackathon().getNome(), i.getTeam().getNome(), i.getSottomissione().getLink())).collect(Collectors.toList());
    }

    /**
     * Metodo che ritorna la lista di richieste pendenti in formato JSON
     *
     * @param nomeUtente il nome utente dell'utente destinatario delle richieste
     * @return una lista di richieste JSON
     */
    @Transactional
    public List<RichiestaDTO> viewRichieste(String nomeUtente) {
        Utente utente = verificaUtenteOrFail(nomeUtente);
        List<Richiesta> listRichieste = repositoryRichiesta.findAllByDestinatario(utente);
        return listRichieste.stream().map(r -> new RichiestaDTO(r.getIdRichiesta(), r.getPayload()))
                .collect(Collectors.toList());
    }

    /**
     * Metodo che ritorna la lista di notifiche destinate all'utente di riferimento
     *
     * @param nomeUtente il nome utente dell'utente destinatario delle notifiche
     * @return la lista di notifiche dto
     */
    @Transactional
    public List<NotificaDTO> viewNotifiche(String nomeUtente) {
        Utente utente = verificaUtenteOrFail(nomeUtente);
        List<Notifica> listNotifiche = repositoryNotifica.findAllByDestinatario(utente);
        return listNotifiche.stream().map(n -> new NotificaDTO(n.getPayload()))
                .collect(Collectors.toList());
    }

    /**
     * Metodo che ritorna la lista di informazioni pubbliche destinate all'utente di riferimento
     *
     * @return la lista di hackathon dto
     */
    @Transactional
    public List<InfoHackathonDTO> viewInfoHackathon() {
        List<Hackathon> listHackathon = repositoryHackathon.findAll();
        List<InfoHackathonDTO> listInfoHackathonDTO = new ArrayList<>();
        for (Hackathon h : listHackathon) {
            int numeroTeamIscritti = h.getIscrizioni().size();
            int postiRimanenti = h.getMaxIscrizioni() - numeroTeamIscritti;
            listInfoHackathonDTO.add(new InfoHackathonDTO(h.getNome(), h.getPeriodo().getDataInizio(), h.getPeriodo().getDataFine(), h.getLuogo(),
                    h.getPremio(), h.getTeamMin(), h.getTeamMax(), h.getRegolamento(), h.getScadenzaIscrizioni(),
                    h.getStato(), numeroTeamIscritti, h.getMaxIscrizioni(), postiRimanenti,
                    h.getRegolamento()));
        }
        return listInfoHackathonDTO;
    }

    @Transactional
    public List<TeamDTO> viewTeamUtenti() {

    List<TeamDTO> risultato = new ArrayList<>();

    List<Team> teams = repositoryTeam.findAll();

    for (Team team : teams) {

        List<String> membri = repositoryMembriTeam
                .findAllByTeam_Nome(team.getNome())
                .stream()
                .map(m -> m.getUtente().getNomeUtente())
                .toList();

        risultato.add(
                new TeamDTO(
                        team.getNome(),
                        membri
                )
        );
    }

    return risultato;
}
}
