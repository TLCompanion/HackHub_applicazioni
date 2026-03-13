package com.example.hackhub.handler;

import com.example.hackhub.boundary.dto.NotificaDTO;
import com.example.hackhub.boundary.dto.RichiestaDTO;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryNotifica;
import com.example.hackhub.repository.RepositoryRichiesta;
import com.example.hackhub.repository.RepositoryUtenti;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisualizzaHandler {

    private final RepositoryHackathon repositoryHackathon;
    private final RepositoryRichiesta repositoryRichiesta;
    private final RepositoryNotifica repositoryNotifica;
    private final RepositoryUtenti repositoryUtenti;

    // TODO sostituire i dto al posto degli oggetti grezzi

    /**
     * Costruttore che inizializza questo handler per visualizzare liste di oggetti
     * @param repositoryHackathon la repository degli hackathon
     */
    public VisualizzaHandler(RepositoryHackathon repositoryHackathon, RepositoryRichiesta repositoryRichiesta,  RepositoryNotifica repositoryNotifica,  RepositoryUtenti repositoryUtenti) {
        this.repositoryHackathon = repositoryHackathon;
        this.repositoryRichiesta = repositoryRichiesta;
        this.repositoryNotifica = repositoryNotifica;
        this.repositoryUtenti = repositoryUtenti;
    }

    /**
     * Metodo che ritorna la lista di team iscritti a un hackathon
     * @param idHackathon l'id dell'hackathon di riferimento
     * @return la lista dei team iscritti
     */
    public List<Team> viewTeam(String idHackathon) {
        Hackathon hackathon = repositoryHackathon.findByIdHackathon(idHackathon)
                .orElseThrow(() -> new RuntimeException("Hackathon non trovato"));
        List<Team> teams = new ArrayList<>();
        for (IscrizioneTeam i : hackathon.getIscrizioni()) teams.add(i.getTeam());
        return teams;
    }

    /**
     * Metodo che ritorna la lista di valutazioni delle sottomissioni consegnate a un hackathon
     * @param idHackathon l'id dell'hackathon di riferimento
     * @return la lista delle valutazioni
     */
    public List<Valutazione> viewValutazioni(String idHackathon) {
        Hackathon hackathon = repositoryHackathon.findByIdHackathon(idHackathon)
                .orElseThrow(() -> new RuntimeException("Hackathon non trovato"));
        List<Valutazione> valutazioni = new ArrayList<>();
        for (IscrizioneTeam i : hackathon.getIscrizioni())
            valutazioni.add(i.getSottomissione().getValutazione());
        return valutazioni;
    }

    /**
     * Metodo che ritorna la lista di sottomissioni consegnate in un hackathon
     * @param idHackathon l'id dell'hackathon di riferimento
     * @return la lista di sottomissioni
     */
    public List<Sottomissione> viewSottomissioni(String idHackathon) {
        Hackathon hackathon = repositoryHackathon.findByIdHackathon(idHackathon)
                .orElseThrow(() -> new RuntimeException("Hackathon non trovato"));
        List<Sottomissione> sottomissioni = new ArrayList<>();
        for (IscrizioneTeam i : hackathon.getIscrizioni()) sottomissioni.add(i.getSottomissione());
        return  sottomissioni;
    }

    /**
     * Metodo che ritorna la lista di iscrizioni effettuate a un hackathon
     * @param idHackathon l'id dell'hackathon di riferimento
     * @return la lista delle iscrizioni
     */
    public List<IscrizioneTeam> viewIscrizioni(String idHackathon) {
        Hackathon hackathon = repositoryHackathon.findByIdHackathon(idHackathon)
                .orElseThrow(() -> new RuntimeException("Hackathon non trovato"));
        return hackathon.getIscrizioni();
    }

    /**
     * Metodo che ritorna la lista di richieste pendenti in formato JSON
     * @param idUtente l'identificativo dell'utente
     * @return una lista di richieste JSON
     */
    public List<RichiestaDTO> viewRichieste(String idUtente) {
        List<Richiesta> listRichieste = repositoryRichiesta.findAllByDestinatario(idUtente);
        List<RichiestaDTO> dtoList = new ArrayList<>();
        for (Richiesta r : listRichieste) dtoList.add(new RichiestaDTO(r.getIdRichiesta(), r.getPayload()));
        return dtoList;
    }

    /**
     * Metodo che ritorna la lista di notifiche destinate all'utente di riferimento
     * @param idUtente l'id dell'utente destinatario
     * @return la lista di notifiche dto
     */
    public List<NotificaDTO> viewNotifiche(String idUtente) {
        List<Notifica> listNotifiche = repositoryNotifica.findAllByDestinatario(
                repositoryUtenti.findByIdUtente(idUtente)
                        .orElseThrow(() -> new RuntimeException("Utente non trovato")));
        List<NotificaDTO> dtoList = new ArrayList<>();
        for (Notifica n : listNotifiche) dtoList.add(new NotificaDTO(n.getPayload()));
        return dtoList;
    }
}
