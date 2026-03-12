package com.example.hackhub.handler;

import com.example.hackhub.boundary.dto.InvitoDTO;
import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.*;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GestisciInvitiHandler {

    private final RepositoryUtenti repositoryUtenti;
    private final RepositoryRichiesta repositoryRichiesta;
    private final RepositoryHackathon repositoryHackathon;
    private final ServizioNotifiche servizioNotifiche;
    private final RepositoryStaff repositoryStaff;
    private final RepositoryMembriTeam repositoryMembriTeam;

    /**
     * Istanzia un handler che si occupa di gestire tutti gli inviti, sia per lo Staff sia per i Team
     * @param repositoryUtenti la repository per gli utente
     * @param repositoryRichiesta la repository per le richieste
     * @param repositoryHackathon la repository per gli hackathon
     * @param servizioNotifiche il servizio che gestisce le notifiche
     */
    public GestisciInvitiHandler(RepositoryUtenti repositoryUtenti, RepositoryRichiesta repositoryRichiesta, RepositoryHackathon repositoryHackathon, ServizioNotifiche servizioNotifiche, RepositoryStaff repositoryStaff, RepositoryMembriTeam repositoryMembriTeam) {
        this.repositoryUtenti = repositoryUtenti;
        this.repositoryRichiesta = repositoryRichiesta;
        this.repositoryHackathon = repositoryHackathon;
        this.servizioNotifiche = servizioNotifiche;
        this.repositoryStaff = repositoryStaff;
        this.repositoryMembriTeam = repositoryMembriTeam;
    }

    /**
     * Metodo che ritorna la lista di richieste pendenti in formato JSON
     * @param idUtente l'identificativo dell'utente
     * @return una lista di inviti JSON
     */
    // TODO questo metodo sarà da estendere con tutti gli altri tipi di Richiesta, assieme al metodo privato in basso
    // TODO questo avverrà con l'implementazione del use case "gestisce proposta di call"
    public List<InvitoDTO> viewInviti(String idUtente) {
        List<Richiesta> listRichieste = repositoryRichiesta.findByDestinatario(idUtente);
        List<InvitoDTO> dtoList = new ArrayList<>();
        for (Richiesta r : listRichieste) dtoList.add(toDto(r));
        return dtoList;
    }

    /**
     * Metodo del boundary che accetta una richiesta di invito Staff
     * @param idUtente l'identificativo dell'utente
     * @param idRichiesta l'identificativo della richeista
     */
    public void accettaRichiesta(String idUtente, String idRichiesta) {
        Utente utente = validazioneUtente(idUtente);
        Richiesta r = validazioneRichiesta(idRichiesta);
        Utente destinatario;

        if (r instanceof InvitoStaff invitoStaff){
            r.accetta();
            destinatario = accettaInvitoStaff(invitoStaff, utente);
        }
        else if (r instanceof InvitoTeam invitoTeam){
            r.accetta();
            destinatario = accettaInvitoTeam(invitoTeam, utente);
        }
        else if(r instanceof PropostaCall propostaCall){
            r.accetta();
            destinatario = accettaCall(propostaCall, utente);
        }else
            throw new ConflictException("La richiesta non appartiene a nessun tipo di invito esistente");
        servizioNotifiche.creaNotifica(List.of(destinatario), TipoNotifica.ACCETTA_RICHIESTA, utente.getNomeUtente() + "ha accettato la tua richiesta");
    }

    private Utente accettaInvitoStaff(InvitoStaff invitoStaff, Utente utente){
        Hackathon hackathon = invitoStaff.getHackathon();
        repositoryHackathon.save(hackathon);
        return trovaOrganizzatore(hackathon);
    }

    private Utente accettaInvitoTeam(InvitoTeam invitoTeam, Utente utente){
        Team team = invitoTeam.getTeam();
        return trovaLeader(team);
    }

    private Utente accettaCall(PropostaCall propostaCall, Utente utente){
        String link = ""; //todo completare, come lo gestiamo sto link
        Team team = repositoryMembriTeam
                .findByUtente_IdUtente(propostaCall.getDestinatario().getIdUtente())
                .orElseThrow(() -> new NotFoundException("L'utente non appartiene a nessun team"))
                .getTeam();
        Staff mentore = repositoryStaff.findByUtente_IdUtente(propostaCall.getMittente());
        CallSlot callSlot = new CallSlot(propostaCall.getPeriodo(), team, mentore, link);
        //todo manca da aggiungerlo al calendario
        return mentore.getUtente();
    }

    private Utente trovaLeader(Team team){
        return team.getMembri().stream().filter(
                m -> m.getRuolo() == RuoloTeam.LEADER).
                map(MembroTeam::getUtente).
                findFirst().
                orElseThrow(
                        () -> new NotFoundException("Utente non trovato"));
    }

    //non so se hanno senso o meno ma sono ripetuti in tutti i metodi quindi mi sembrava meglio fare così
    private Utente validazioneUtente(String idUtente){
        return repositoryUtenti.findByIdUtente(idUtente)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    private Richiesta validazioneRichiesta(String idRichiesta){
        return repositoryRichiesta.findById(idRichiesta)
                .orElseThrow(() -> new RuntimeException("Invito scaduto"));
    }

    private Utente trovaOrganizzatore(Hackathon hackathon){
        return hackathon.getStaff().stream().filter(
                s -> s.getRuolo() == RuoloStaff.ORGANIZZATORE)
                .map(Staff::getUtente)
                .findFirst()
                .orElseThrow(() ->
                new NotFoundException("Organizzatore non trovato"));
    }

    public void rifiutaRichiesta(String idRichiesta){
        Richiesta r = validazioneRichiesta(idRichiesta);
        r.rifiuta();
        if (!(r instanceof InvitoStaff invitoStaff))
            throw new ConflictException("La richiesta non è un invito staff");
        servizioNotifiche.creaNotifica(List.of(trovaOrganizzatore(invitoStaff.getHackathon())), TipoNotifica.RIFIUTO_RICHIESTA, "La richiesta è stata rifiutata");
    }

    /**
     * Crea un nuovo Dto in base al tipo della richiesta
     * @param richiesta la richiesta
     * @return un nuovo dto, null se il tipo della richiesta non corrisponde con nessun tipo esistente
     */
    private InvitoDTO toDto(Richiesta richiesta) {
        if (richiesta instanceof InvitoStaff is)
            return new InvitoDTO(is.getDestinatario().getIdUtente(), "INVITO_STAFF", null, is.getHackathon().getNome(), is.getRuolo());

        if (richiesta instanceof InvitoTeam it)
            return new InvitoDTO(it.getDestinatario().getIdUtente(), "INVITO_TEAM", it.getTeam().getNome(), null, null);

        return null;
    }
}
