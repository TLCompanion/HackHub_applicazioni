package com.example.hackhub.handler;

import com.example.hackhub.boundary.dto.InvitiDTO;
import com.example.hackhub.domain.implementazione.InvitoStaff;
import com.example.hackhub.domain.implementazione.InvitoTeam;
import com.example.hackhub.domain.implementazione.Richiesta;
import com.example.hackhub.domain.implementazione.Utente;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryRichiesta;
import com.example.hackhub.repository.RepositoryUtenti;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GestisciInvitiHandler {

    private final RepositoryUtenti repositoryUtenti;
    private final RepositoryRichiesta repositoryRichiesta;
    private final RepositoryHackathon repositoryHackathon;
    private final ServizioNotifiche servizioNotifiche;

    /**
     * Istanzia un handler che si occupa di gestire tutti gli inviti, sia per lo Staff sia per i Team
     * @param repositoryUtenti
     * @param repositoryRichiesta
     * @param repositoryHackathon
     * @param servizioNotifiche
     */
    public GestisciInvitiHandler(RepositoryUtenti repositoryUtenti, RepositoryRichiesta repositoryRichiesta, RepositoryHackathon repositoryHackathon, ServizioNotifiche servizioNotifiche) {
        this.repositoryUtenti = repositoryUtenti;
        this.repositoryRichiesta = repositoryRichiesta;
        this.repositoryHackathon = repositoryHackathon;
        this.servizioNotifiche = servizioNotifiche;
    }

    /**
     * Metodo che ritorna la lista di richieste pendenti in formato JSON
     * @param idUtente
     * @return una lista di inviti JSON
     */
    // TODO questo metodo sarà da estendere con tutti gli altri tipi di Richiesta, assieme al metodo privato in basso
    // TODO questo avverrà con l'implementazione del use case "gestisce proposta di call"
    public List<InvitiDTO> viewInviti(String idUtente) {
        List<Richiesta> listRichieste = repositoryRichiesta.findByDestinatario(idUtente);
        List<InvitiDTO> dtoList = new ArrayList<>();
        for (Richiesta r : listRichieste) dtoList.add(toDto(r));
        return dtoList;
    }

    /**
     * Metodo del boundary che accetta una richiesta di invito Staff
     * @param idUtente
     * @param idRichiesta
     */
    public void accettaInvitoStaff(String idUtente, String idRichiesta) {
        Utente utente = repositoryUtenti.findByIdUtente(idUtente)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        Richiesta r = repositoryRichiesta.findById(idRichiesta)
                .orElseThrow(() -> new RuntimeException("Richiesta scaduta"));
        r.accetta();
        // TODO da completare
    }

    /**
     * Metodo del boundary che accetta una richiesta di invito Team
     * @param idUtente
     * @param idRichiesta
     */
    public void accettaInvitoTeam(String idUtente, String idRichiesta) {
        // TODO completare
    }

    public void rifiutaInvitoStaff(String idUtente, String idRichiesta) {
        // TODO completare
    }

    public void rifiutaInvitoTeam(String idUtente, String idRichiesta) {
        // TODO completare
    }

    private InvitiDTO toDto(Richiesta richiesta) {
        if (richiesta instanceof InvitoStaff is)
            return new InvitiDTO(is.getDestinatario().getIdUtente(), "INVITO_STAFF", null, is.getHackathon().getNome(), is.getRuolo());

        if (richiesta instanceof InvitoTeam it)
            return new InvitiDTO(it.getDestinatario().getIdUtente(), "INVITO_TEAM", it.getTeam().getNome(), null, null);

        return null;
    }
}
