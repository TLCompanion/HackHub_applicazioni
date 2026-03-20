package com.example.hackhub.handler;

import com.example.hackhub.boundary.dto.*;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisualizzaHandler {

    private final RepositoryHackathon repositoryHackathon;
    private final RepositoryRichiesta repositoryRichiesta;
    private final RepositoryNotifica repositoryNotifica;
    private final RepositoryUtenti repositoryUtenti;
    private final RepositoryStaff repositoryStaff;

    /**
     * Costruttore che inizializza questo handler per visualizzare liste di oggetti
     * @param repositoryHackathon la repository degli hackathon
     */
    public VisualizzaHandler(RepositoryHackathon repositoryHackathon, RepositoryRichiesta repositoryRichiesta, RepositoryNotifica repositoryNotifica, RepositoryUtenti repositoryUtenti, RepositoryStaff repositoryStaff) {
        this.repositoryHackathon = repositoryHackathon;
        this.repositoryRichiesta = repositoryRichiesta;
        this.repositoryNotifica = repositoryNotifica;
        this.repositoryUtenti = repositoryUtenti;
        this.repositoryStaff = repositoryStaff;
    }

    //TODO nell'uml aggiungere questi controlli con le eccezioni per tutti i casi d'uso che utilizzano questo metodo di

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
        return repositoryUtenti.findByNomeUtente(nomeUtente).orElseThrow(() ->
                new NotFoundException("Utente non trovato"));
    }

    /**
     * Metodo che ritorna la lista di valutazioni delle sottomissioni consegnate a un hackathon
     * @param idHackathon l'id dell'hackathon di riferimento
     * @return la lista delle valutazioni
     */
    public List<ValutazioneRequest> viewValutazioni(String nomeUtente, String idHackathon) {
        Hackathon hackathon = validaAutorizzazioni(nomeUtente, idHackathon);
        List<Valutazione> valutazioni = new ArrayList<>();
        for (IscrizioneTeam i : hackathon.getIscrizioni())
            valutazioni.add(i.getSottomissione().getValutazione());
        return valutazioni.stream().map(v -> new ValutazioneRequest(v.getDescrizione(),
                v.getVoto())).collect(Collectors.toList());
    }

    /**
     * Metodo che ritorna la lista di sottomissioni consegnate in un hackathon
     * @param idHackathon l'id dell'hackathon di riferimento
     * @return la lista di sottomissioni
     */
    public List<SottomissioneDTO> viewSottomissioni(String nomeUtente, String idHackathon) {
        Hackathon hackathon = validaAutorizzazioni(nomeUtente, idHackathon);
        List<Sottomissione> sottomissioni = new ArrayList<>();
        for (IscrizioneTeam i : hackathon.getIscrizioni()) sottomissioni.add(i.getSottomissione());

        return  sottomissioni.stream().map(s -> new SottomissioneDTO
                (s.getLink(), s.getValutazione().getDescrizione(), s.getValutazione().getVoto())).collect(Collectors.toList());
    }

    /**
     * Metodo che ritorna la lista di iscrizioni effettuate a un hackathon
     * @param idHackathon l'id dell'hackathon di riferimento
     * @return la lista delle iscrizioni
     */
    public List<IscrizioneTeamDTO> viewIscrizioni(String nomeUtente, String idHackathon) {
        Hackathon hackathon = validaAutorizzazioni(nomeUtente, idHackathon);
        return hackathon.getIscrizioni().stream().map(i -> new IscrizioneTeamDTO
                (i.getHackathon().getNome(), i.getTeam().getNome(), i.getSottomissione().getLink())).collect(Collectors.toList());
    }

    /**
     * Metodo che ritorna la lista di richieste pendenti in formato JSON
     * @param nomeUtente il nome utente dell'utente destinatario delle richieste
     * @return una lista di richieste JSON
     */
    public List<RichiestaDTO> viewRichieste(String nomeUtente) {
        Utente utente = verificaUtenteOrFail(nomeUtente);
        List<Richiesta> listRichieste = repositoryRichiesta.findAllByDestinatario(utente);
        return listRichieste.stream().map(r -> new RichiestaDTO(r.getIdRichiesta(), r.getPayload()))
                .collect(Collectors.toList());
    }

    /**
     * Metodo che ritorna la lista di notifiche destinate all'utente di riferimento
     * @param nomeUtente il nome utente dell'utente destinatario delle notifiche
     * @return la lista di notifiche dto
     */
    public List<NotificaDTO> viewNotifiche(String nomeUtente) {
        Utente utente = verificaUtenteOrFail(nomeUtente);
        List<Notifica> listNotifiche = repositoryNotifica.findAllByDestinatario(utente);
        return listNotifiche.stream().map(n -> new NotificaDTO(n.getPayload()))
                .collect(Collectors.toList());
    }

    /**
     * Metodo che ritorna la lista di informazioni pubbliche destinate all'utente di riferimento
     * @param nomeUtente il nome dell'utente destinatario delle info
     * @return la lista di hackathon dto
     */
    public List<InfoHackathonDTO> viewInfoHackathon(String nomeUtente){
        verificaUtenteOrFail(nomeUtente);
        List<Hackathon> listHackathon = repositoryHackathon.findAll();
        List<InfoHackathonDTO> listInfoHackathonDTO = new ArrayList<>();
        for (Hackathon h : listHackathon){
            int numeroTeamIscritti = h.getIscrizioni().size();
            int postiRimanenti = h.getMaxIscrizioni() - numeroTeamIscritti;
            listInfoHackathonDTO.add(new InfoHackathonDTO(h.getNome(), h.getPeriodo().getDataInizio(), h.getPeriodo().getDataFine(), h.getLuogo(),
                    h.getPremio(), h.getTeamMin(), h.getTeamMax(), h.getRegolamento(), h.getScadenzaIscrizioni(),
                    h.getStato(), numeroTeamIscritti, h.getMaxIscrizioni(), postiRimanenti,
                    h.getRegolamento()));
        }
        return listInfoHackathonDTO;
    }
}
