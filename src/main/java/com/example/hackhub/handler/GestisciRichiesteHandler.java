package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.*;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestisciRichiesteHandler {

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
    public GestisciRichiesteHandler(RepositoryUtenti repositoryUtenti, RepositoryRichiesta repositoryRichiesta, RepositoryHackathon repositoryHackathon, ServizioNotifiche servizioNotifiche, RepositoryStaff repositoryStaff, RepositoryMembriTeam repositoryMembriTeam) {
        this.repositoryUtenti = repositoryUtenti;
        this.repositoryRichiesta = repositoryRichiesta;
        this.repositoryHackathon = repositoryHackathon;
        this.servizioNotifiche = servizioNotifiche;
        this.repositoryStaff = repositoryStaff;
        this.repositoryMembriTeam = repositoryMembriTeam;
    }

    /**
     * Metodo del boundary che accetta una richiesta di invito Staff
     * @param nomeUtente il nome utente dell'utente che accetta la richiesta
     * @param idRichiesta l'identificativo della richeista
     */
    @Transactional
    public void accettaRichiesta(String nomeUtente, String idRichiesta) {
        Utente utente = validazioneUtente(nomeUtente);
        Richiesta r = validazioneRichiesta(idRichiesta);
        Utente destinatario;

        switch (r) {
            case InvitoStaff invitoStaff -> {
                r.accetta();
                destinatario = accettaInvitoStaff(nomeUtente, invitoStaff);
            }
            case InvitoTeam invitoTeam -> {
                r.accetta();
                destinatario = accettaInvitoTeam(nomeUtente, invitoTeam);
            }
            case PropostaCall propostaCall -> {
                r.accetta();
                destinatario = accettaCall(nomeUtente, propostaCall);
            }
            default -> throw new ConflictException("La richiesta non appartiene a nessun tipo di invito esistente");
        }
        servizioNotifiche.creaNotifica(destinatario, TipoNotifica.ACCETTA_RICHIESTA, utente.getNomeUtente() + "ha accettato la tua richiesta");
    }

    @Transactional
    public void rifiutaRichiesta(String nomeUtente, String idRichiesta){
        validazioneUtente(nomeUtente);
        Richiesta r = validazioneRichiesta(idRichiesta);
        r.rifiuta();
        servizioNotifiche.creaNotifica(r.getDestinatario(), TipoNotifica.RIFIUTO_RICHIESTA, "La richiesta è stata rifiutata");
    }

    private Utente accettaInvitoStaff(String nomeUtente, InvitoStaff invitoStaff){
        validazioneUtente(nomeUtente);
        Hackathon hackathon = invitoStaff.getHackathon();
        repositoryHackathon.save(hackathon);
        return trovaOrganizzatore(hackathon);
    }

    private Utente accettaInvitoTeam(String nomeUtente, InvitoTeam invitoTeam){
        validazioneUtente(nomeUtente);
        Team team = invitoTeam.getTeam();
        return trovaLeader(team);
    }

    private Utente accettaCall(String nomeUtente, PropostaCall propostaCall){
        validazioneUtente(nomeUtente);
        String link = ""; //todo completare, come lo gestiamo sto link
        Team team = repositoryMembriTeam
                .findByUtente_NomeUtente(propostaCall.getDestinatario().getNomeUtente())
                .orElseThrow(() -> new NotFoundException("L'utente non appartiene a nessun team"))
                .getTeam();
        Staff mentore = repositoryStaff.findByUtente_NomeUtente(propostaCall.getMittente())
                .orElseThrow(() -> new NotFoundException("Mentore non trovato"));
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
    private Utente validazioneUtente(String nomeUtente){
        return repositoryUtenti.findByNomeUtente(nomeUtente)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
    }

    private Richiesta validazioneRichiesta(String idRichiesta){
        return repositoryRichiesta.findById(idRichiesta)
                .orElseThrow(() -> new NotFoundException("Invito scaduto"));
    }

    private Utente trovaOrganizzatore(Hackathon hackathon){
        return hackathon.getStaff().stream().filter(
                s -> s.getRuolo() == RuoloStaff.ORGANIZZATORE)
                .map(Staff::getUtente)
                .findFirst()
                .orElseThrow(() ->
                new NotFoundException("Organizzatore non trovato"));
    }
}
