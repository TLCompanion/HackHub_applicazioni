package com.example.hackhub.servizi;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.TipoRichiesta;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryNotifica;
import com.example.hackhub.repository.RepositoryRichiesta;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Pattern: Singleton, gestione delle notifiche
 */
@Service
public class ServizioNotifiche {

//    private final FactoryPayload factoryPayload;
//    private final RepositoryRichiesta repositoryRichiesta;
//    private final RepositoryNotifica repositoryNotifica;
//
//    /**
//     * Costruzione di un'entitò di ServizioNotifiche
//     */
//    public ServizioNotifiche(RepositoryRichiesta repositoryRichiesta, FactoryPayload factoryPayload, RepositoryNotifica repositoryNotifica) {
//        this.repositoryRichiesta = repositoryRichiesta;
//        this.factoryPayload = factoryPayload;
//        this.repositoryNotifica = repositoryNotifica;
//    }
//
//    /**
//     * Invia una richiesta da parte di un mentore, un'organizzatore o un team
//     */
//    public void inviaInvitoStaff (Hackathon hackathon, Map<Utente, RuoloStaff> destinatari) {
//
//        Utente organizzatore = hackathon.getStaff().stream()
//                .filter(s -> s.getRuolo() == RuoloStaff.ORGANIZZATORE)
//                .map(Staff::getUtente)
//                .findFirst()
//                .orElseThrow(() ->
//                        new NotFoundException("Organizzatore non trovato"));
//
//        for (Map.Entry<Utente, RuoloStaff> entry : destinatari.entrySet()) {
//
//            Utente destinatario = entry.getKey();
//            RuoloStaff ruolo = entry.getValue();
//
//            Payload payload = factoryPayload.creaPayloadInvitoStaff(hackathon.getNome(), ruolo);
//
//            Richiesta richiesta = new Richiesta(organizzatore.getIdUtente(), payload, TipoRichiesta.INVITO_STAFF, List.of(destinatario));
//
//            repositoryRichiesta.save(richiesta);
//        }
//    }
//
//    /**
//     * Invia una proposta di call al leader del team che ha richiesto una call
//     * @param mittente il mittente della proposta di call
//     * @param hackathon l'hackathon associato
//     * @param leaderTeam il leader del team
//     * @param periodo il periodo proposto
//     */
//    public void inviaPropostaCall (String mittente, Hackathon hackathon, Utente leaderTeam, Periodo periodo) {
//        Payload payload = factoryPayload.creaPayloadPropostaCall(hackathon.getNome(), periodo);
//        Richiesta richiesta = new Richiesta(mittente, payload, TipoRichiesta.PROPOSTA_CALL, List.of(leaderTeam));
//        repositoryRichiesta.save(richiesta);
//    }
//
//    /**
//     * Invia una notifica generica a un utente, un team o un'organizzazione
//     */
//    public void inviaNotifica(List<Utente> destinatari, TipoNotifica tipoNotifica) {
//        //todo ho fatto così per fare prima anche se questo messaggio andrebbe creato nella factory
//        Payload payload = factoryPayload.creaPayloadNotificaGenerica("L'hackathon a cui ti sei iscritto è " +
//                "concluso, grazie per aver partecipato!");
//        Notifica notifica = new Notifica(payload, destinatari, tipoNotifica);
//        repositoryNotifica.save(notifica);
//    }
//
//    private void gestisciValutazioneConclusa(Evento evento) {
//        List<IscrizioneTeam> iscrizioni = repositoryIscrizioniTeam.findAllByHackathon(
//                (Hackathon) evento.data.get("hackathon"));
//        Set<Utente> destinatariSet = new HashSet<>();
//        for (IscrizioneTeam iscrizione : iscrizioni) {
//            Team team = iscrizione.getTeam();
//            for (MembroTeam m : team.getMembri()) {
//                destinatariSet.add(m.getUtente());
//            }
//        }
//        chiamaServizioNotifiche(destinatariSet, evento.getTipo());
//    }
//
//    private void chiamaServizioNotifiche(Set<Utente> destinatariSet, TipoNotifica tipo) {
//        List<Utente> destinatari = new ArrayList<>(destinatariSet);
//        servizioNotifiche.inviaNotifica(destinatari, tipo);
//    }
}
