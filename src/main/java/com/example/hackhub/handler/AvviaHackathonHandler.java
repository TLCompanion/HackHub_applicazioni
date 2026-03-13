package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
public class AvviaHackathonHandler {

    private final RepositoryHackathon repositoryHackathon;
    private final ServizioNotifiche servizioNotifiche;

    public AvviaHackathonHandler(RepositoryHackathon repositoryHackathon, ServizioNotifiche servizioNotifiche) {
        this.repositoryHackathon = repositoryHackathon;
        this.servizioNotifiche = servizioNotifiche;
    }

    /**
     * Metodo che avvia un'hackathon
     * @param hackathon l'hackathon da avviare
     */
    public void avviaHackathon(Hackathon hackathon) {
        repositoryHackathon.findByNome(hackathon.getNome()).orElseThrow(() -> new NotFoundException("Hackathon non trovato"));
        eseguiControlloScadenze(hackathon);
        List<Hackathon> hackathons = repositoryHackathon.findAll().stream()
                .filter(h -> h.getPeriodo().getDataInizio().equals(LocalDate.now()))
                .toList();
        for(Hackathon h: hackathons){
            h.avviaHackathon();
            notificaUtenti(hackathon);
        }
    }

    /**
     * Controlla che tutte le scadenze siano rispettate e notifica l'organizzatore in caso contrario
     * @param hackathon l'hackathon
     * @throws ConflictException se le scadenze non sono rispettate
     */
    private void eseguiControlloScadenze(Hackathon hackathon) {
        if (!hackathon.getPeriodo().getDataInizio().equals(LocalDate.now()))
         {
            servizioNotifiche.creaNotifica(trovaOrganizzatore(hackathon), TipoNotifica.IMPOSSIBILE_AVVIARE_HACKATHON, "Impossibile avviare l'hackathon " + hackathon.getNome());
            throw new ConflictException("Scadenze non rispettate");
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
