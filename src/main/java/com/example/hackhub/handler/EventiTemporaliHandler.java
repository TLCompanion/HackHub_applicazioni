package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.IscrizioniChiuse;
import com.example.hackhub.domain.implementazione.statePattern.StatoHackathon;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
     * Metodo che avvia tutti gli hackathon che devono essere avviati se i requisiti sono rispettati, e notifica gli
     * utenti dell'inizio dell'hackathon, in caso di errori notifica l'organizzatore dell'impossibilità di avviare l'hackathon
     */
    public void avviaHackathon() {
        LocalDateTime now = LocalDateTime.now();
        List<Hackathon> hackathonDaAvviare = repositoryHackathon.findHackathonDaAvviare(IscrizioniChiuse.INSTANCE,
                now.toLocalDate(), now.toLocalTime());
        for(Hackathon h: hackathonDaAvviare){
            try {
                h.avviaHackathon();
                repositoryHackathon.save(h);
                notificaUtenti(h);
            } catch (ConflictException e) {
                servizioNotifiche.creaNotifica(trovaOrganizzatore(h), TipoNotifica.IMPOSSIBILE_AVVIARE_HACKATHON,
                        "Impossibile avviare l'hackathon " + h.getNome());
            }
        }
    }

    //TODO mettere i metodi per gli altri eventi temporali

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
