package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.StatoEnum;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.*;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.eccezioni.TransizioneNonConsentitaException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class EventiTemporaliHandler {

    private final RepositoryHackathon repositoryHackathon;
    private final ServizioNotifiche servizioNotifiche;

    public EventiTemporaliHandler(RepositoryHackathon repositoryHackathon, ServizioNotifiche servizioNotifiche) {
        this.repositoryHackathon = repositoryHackathon;
        this.servizioNotifiche = servizioNotifiche;
    }

    /**
     * Metodo che gestisce tutte le scadenze temporali degli hackathon
     */
    public void gestisciScadenzeTemporali() {
        avviaHackathon();
        chiudiIscrizioni();
        iniziaValutazione();
    }

    /**
     * Metodo che avvia tutti gli hackathon che devono essere avviati se i requisiti sono rispettati, e notifica gli
     * utenti dell'inizio dell'hackathon, in caso di errori notifica l'organizzatore dell'impossibilità di avviare l'hackathon
     */
    public void avviaHackathon() {
        LocalDateTime now = LocalDateTime.now();
        List<Hackathon> hackathonDaAvviare = repositoryHackathon.findHackathonDaAvviare(StatoEnum.ISCRIZIONI_CHIUSE,
                now.toLocalDate(), now.toLocalTime());
        for(Hackathon h: hackathonDaAvviare){
            try {
                h.avviaHackathon();
                h.setStatoEnum(InCorso.INSTANCE);
                repositoryHackathon.save(h);
                notificaUtenti(h);
            } catch (ConflictException e) {
                servizioNotifiche.creaNotifica(trovaOrganizzatore(h), TipoNotifica.IMPOSSIBILE_AVVIARE_HACKATHON,
                        "Impossibile avviare l'hackathon " + h.getNome());
            }
        }
    }

    /**
     * Metodo che chiude le iscrizioni di tutti gli hackathon il cui termine è passato
     */
    private void chiudiIscrizioni() {
        StatoHackathon stato = IscrizioniAperte.INSTANCE;
        List<Hackathon> hackathonDaChiudere = repositoryHackathon.findHackathonDaChiudere(StatoEnum.ISCRIZIONI_APERTE, LocalDateTime.now());
        for (Hackathon h : hackathonDaChiudere) {
            try { h.chiudiIscrizioni(); }
            catch (TransizioneNonConsentitaException e)
            { throw new RuntimeException("Impossibile chiudere le iscrizioni dell'hackathon " + h.getNome()); }
            h.setStatoEnum(IscrizioniChiuse.INSTANCE);
            repositoryHackathon.save(h);
        }
    }

    /**
     * Metodo che blocca la consegna delle sottomissioni, dando inizio alla fase di valutazione delle
     * sottomissioni dei vari hackathon
     */
    private void iniziaValutazione() {
        StatoHackathon stato = InCorso.INSTANCE;
        List<Hackathon> hackathonDaValutare = repositoryHackathon.findHackathonDaValutare(StatoEnum.IN_CORSO, LocalDateTime.now());
        for (Hackathon h : hackathonDaValutare) {
            try { h.avviaValutazione(); }
            catch (TransizioneNonConsentitaException e)
            { throw new RuntimeException("Impossibile avviare la valutazione dell'hackathon " + h.getNome()); }
            h.setStatoEnum(ValutazioneInCorso.INSTANCE);
            repositoryHackathon.save(h);
        }
    }

    /**
     * Trova l'organizzatore dell'hackathon
     * @param hackathon l'hackathon
     * @return l'organizzatore
     */
    private Utente trovaOrganizzatore(Hackathon hackathon){
        return hackathon.getStaff().stream().filter(
                        s -> s.getRuolo() == RuoloStaff.ORGANIZZATORE)
                .map(Staff::getUtente)
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException("Organizzatore non trovato"));
    }

    /**
     * Notifica gli utenti dell'inizio dell'hackathon
     * @param hackathon l'hackathon
     */
    private void notificaUtenti(Hackathon hackathon) {
        List<Utente> staff = hackathon.getStaff().stream().map(Staff::getUtente).toList();
        List<Utente> team = hackathon.getIscrizioni().stream().map(IscrizioneTeam::getTeam).flatMap(t -> t.getMembri().stream()).map(MembroTeam::getUtente).toList();
        List<Utente> destinatari = Stream.concat(staff.stream(), team.stream()).toList();
        for (Utente u : destinatari) {
            servizioNotifiche.creaNotifica(u, TipoNotifica.AVVIO_HACKATHON, "L'hackathon " + hackathon.getNome() + " è iniziato");
        }
    }
}
