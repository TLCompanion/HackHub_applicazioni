package com.example.hackhub.handler;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.domain.implementazione.Staff;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.domain.implementazione.Utente;
import com.example.hackhub.domain.implementazione.statePattern.InCorso;
import com.example.hackhub.domain.implementazione.statePattern.IscrizioniAperte;
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
    private final RepositoryHackathon repositoryHackathon;

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
        this.repositoryHackathon = repositoryHackathon;
    }

    /**
     * Segnala che un team ha violato il regolamento
     * @param idOrganizzatore l'organizzatore da notificare
     * @param team il team che ha violato il regolamento
     */
    public void segnalaViolazione(String idOrganizzatore, Team team){
        if (!repositoryTeam.existsById(team.getIdTeam())) {
            throw new IllegalArgumentException("Team non trovato");
        }
        Staff organizzatore = repositoryStaff.getStaffById(idOrganizzatore)
                .orElseThrow(() -> new IllegalArgumentException("Organizzatore non trovato"));
        servizioNotifiche.creaNotifica(organizzatore.getUtente(), VIOLAZIONE_REGOLAMENTO,
                "Il team " + team.getNome() + " ha violato il regolamento dell'hackathon");
    }

    public void nominaMentori(String nomeUtente, String nomeUtenteDaInvitare){
        Staff organizzatore = repositoryStaff.findByUtente_NomeUtente(nomeUtente)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        if (organizzatore.getRuolo() != RuoloStaff.ORGANIZZATORE) {
            throw new IllegalArgumentException("Solo gli organizzatori possono nominare mentori");
        }
        Utente staffDaInvitare = repositoryUtenti.findByNomeUtente(nomeUtenteDaInvitare)
                .orElseThrow(() -> new IllegalArgumentException("Utente da invitare non trovato"));
        Hackathon hackathon = organizzatore.getHackathon();
        if(!(hackathon.getStato() instanceof IscrizioniAperte)) {
            throw new IllegalStateException("Non è possibile nominare mentori se le iscrizioni non sono aperte");
        }
        if (repositoryStaff.findByUtente_NomeUtente(nomeUtenteDaInvitare).isPresent()) {
            throw new IllegalArgumentException("L'utente da invitare è già uno staff");
        }
        servizioNotifiche.creaInvitoStaff(nomeUtente, staffDaInvitare, hackathon, RuoloStaff.MENTORE);
    }
}
