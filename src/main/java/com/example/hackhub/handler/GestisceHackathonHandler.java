package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.domain.implementazione.Staff;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.domain.implementazione.Utente;
import com.example.hackhub.domain.implementazione.statePattern.InCorso;
import com.example.hackhub.domain.implementazione.statePattern.IscrizioniAperte;
import com.example.hackhub.eccezioni.BadRequestException;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryStaff;
import com.example.hackhub.repository.RepositoryTeam;
import com.example.hackhub.repository.RepositoryUtenti;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;

import static com.example.hackhub.domain.TipoNotifica.VIOLAZIONE_REGOLAMENTO;

@Service
public class GestisceHackathonHandler {

    private final ServizioNotifiche servizioNotifiche;
    private final RepositoryStaff repositoryStaff;
    private final RepositoryTeam repositoryTeam;
    private final RepositoryUtenti repositoryUtenti;

    /**
     * Crea una nuova istanza di un handler per la gestione degli hackathon
     * @param servizioNotifiche il servizio notifiche
     * @param repositoryStaff la repository dello staff
     * @param repositoryTeam la repository dei team
     */
    public GestisceHackathonHandler(ServizioNotifiche servizioNotifiche, RepositoryStaff repositoryStaff, RepositoryTeam repositoryTeam, RepositoryUtenti repositoryUtenti, RepositoryHackathon repositoryHackathon) {
        this.servizioNotifiche = servizioNotifiche;
        this.repositoryStaff = repositoryStaff;
        this.repositoryTeam = repositoryTeam;
        this.repositoryUtenti = repositoryUtenti;
    }

    /**
     * Segnala che un team ha violato il regolamento
     * @param nomeOrganizzatore l'organizzatore da notificare
     * @param nomeTeam il nome del team che ha violato il regolamento
     */
    public void segnalaViolazione(String nomeOrganizzatore, String nomeTeam){
        Team team = repositoryTeam.findByNome(nomeTeam).orElseThrow(() -> new NotFoundException("Team non trovato"));

        Staff organizzatore = repositoryStaff.findByUtente_NomeUtente(nomeOrganizzatore)
                .orElseThrow(() -> new NotFoundException("Organizzatore non trovato"));
        servizioNotifiche.creaNotifica(organizzatore.getUtente(), VIOLAZIONE_REGOLAMENTO,
                "Il team " + team.getNome() + " ha violato il regolamento dell'hackathon");
    }

    /**
     * Metodo per invitare un utente a diventare mentore di un hackathon. Solo gli organizzatori possono nominare mentori e le iscrizioni devono essere aperte
     * @param nomeUtente il nome dell'organizzatore
     * @param nomeUtenteDaInvitare il nome dell'utente da invitare
     */
    public void nominaMentori(String nomeUtente, String nomeUtenteDaInvitare){
        Staff organizzatore = repositoryStaff.findByUtente_NomeUtente(nomeUtente)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
        if (organizzatore.getRuolo() != RuoloStaff.ORGANIZZATORE) {
            throw new ConflictException("Solo gli organizzatori possono nominare mentori");
        }
        Utente staffDaInvitare = repositoryUtenti.findByNomeUtente(nomeUtenteDaInvitare)
                .orElseThrow(() -> new NotFoundException("Utente da invitare non trovato"));
        Hackathon hackathon = organizzatore.getHackathon();
        if(!(hackathon.getStato() instanceof IscrizioniAperte)) {
            throw new ConflictException("Non è possibile nominare mentori se le iscrizioni non sono aperte");
        }
        if (repositoryStaff.findByUtente_NomeUtente(nomeUtenteDaInvitare).isPresent()) {
            throw new BadRequestException("L'utente da invitare è già nello staff");
        }
        servizioNotifiche.creaInvitoStaff(nomeUtente, staffDaInvitare, hackathon, RuoloStaff.MENTORE);
    }
}
