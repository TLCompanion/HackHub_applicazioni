package com.example.hackhub.handler;

import com.example.hackhub.boundary.dto.HackathonRequest;
import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.TipoRichiesta;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.servizi.HackathonBuilder;
import com.example.hackhub.servizi.ServizioNotifiche;
import com.example.hackhub.eccezioni.ForbiddenException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryStaff;
import com.example.hackhub.repository.RepositoryUtenti;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CreaHackathonHandler {

    private final RepositoryUtenti repositoryUtenti;
    private final RepositoryHackathon repositoryHackathon;
    private final RepositoryStaff repositoryStaff;
    private final ServizioNotifiche servizioNotifiche;

    /**
     * Crea un handler che si occupa di gestire tutte le operazioni necessarie per creare un hackathon
     * @param repositoryUtenti la repository per recuperare gli utenti che saranno organizzatori, mentori e giudici
     * @param repositoryHackathon la repository per salvare l'hackathon creato
     * @param repositoryStaff la repository per salvare i membri dello staff associati all'hackathon
     * @param servizioNotifiche il servizio per inviare le notifiche agli utenti invitati come giudici e mentori
     */
    public CreaHackathonHandler(RepositoryUtenti repositoryUtenti, RepositoryHackathon
            repositoryHackathon, RepositoryStaff repositoryStaff, ServizioNotifiche servizioNotifiche) {
        this.repositoryUtenti = repositoryUtenti;
        this.repositoryHackathon = repositoryHackathon;
        this.repositoryStaff = repositoryStaff;
        this.servizioNotifiche = servizioNotifiche;
    }

    /**
     * Avvia la creazione di un hackathon, verificando che il nome dell'hackathon non sia già esistente. Se tutte le
     * verifiche passano, imposta i dati dell'hackathon usando il builder, imposta l'organizzatore, invia gli inviti a
     * giudice e mentori, e salva l'hackathon nel database.
     * @param request la richiesta di creazione
     * @param idUtente l'identificativo associato all'utente
     */
    @Transactional
    public void avviaCreazioneHackathon(HackathonRequest request, String idUtente) {
        if (repositoryHackathon.existsByNome(request.nome())){
            throw new ForbiddenException("Esiste già un hackathon con questo nome");
        }
        HackathonBuilder builder = new HackathonBuilder();
        builder.reset();
        buildSteps(builder, request);
        Hackathon hackathon = builder.getRisultato();
        gestisciOrganizzatore(idUtente, hackathon);
        gestisciInvitiStaff(hackathon, request.nomeMentori(), request.nomeGiudice());
        repositoryHackathon.save(hackathon);
    }

    private void buildSteps(HackathonBuilder builder, HackathonRequest request) {
        builder.impostaNome(request.nome());
        Periodo periodo = new Periodo(request.dataInizio(), request.dataFine());
        builder.impostaPeriodo(periodo);
        builder.impostaLuogo(request.luogo());
        builder.impostaPremio(request.premio());
        builder.impostaTeamMin(request.teamMin());
        builder.impostaTeamMax(request.teamMax());
        builder.impostaRegolamento(request.regolamento());
        builder.impostaScadenzaIscrizioni(request.scadenzaIscrizioni());
        builder.impostaMaxIscrizioni(request.maxIscrizioni());
    }

    /**
     * Gestione degli inviti allo staff per un hackathon
     * @param hackathon l'hackathon
     * @param nomiMentori i nomi degli utenti che si vogliono invitare come mentori
     * @param nomeGiudice il nome dell'utente che si vuole invitare come giudice
     */
    private void gestisciInvitiStaff(Hackathon hackathon, List<String> nomiMentori, String nomeGiudice) {
        Map<Utente, RuoloStaff> destinatari = gestisciStaff(nomiMentori, nomeGiudice);
        List<Utente> utentiDestinatari = destinatari.keySet().stream().toList();
        Utente organizzatore = hackathon.getStaff().stream()
                .filter(s -> s.getRuolo() == RuoloStaff.ORGANIZZATORE)
                .map(Staff::getUtente)
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException("Organizzatore non trovato"));
        String messaggio = "Invito per diventare staff";
        servizioNotifiche.creaRichiesta(organizzatore.getIdUtente(), utentiDestinatari, TipoRichiesta.INVITO_STAFF, messaggio, null);
    }

    /**
     * Controlla che i nomi degli utenti legati allo staff siano presenti nel sistema
     * @param nomiMentori i nomi dei mentori
     * @param nomeGiudice il nome del giudice
     * @return una nuova HashMap che associa l'utente esistente al suo ruolo
     */
    private Map<Utente, RuoloStaff> gestisciStaff(List<String> nomiMentori, String nomeGiudice) {
        List<Utente> mentori = nomiMentori.stream().map(nome -> repositoryUtenti.findByNomeUtente(nome).orElseThrow(() ->
                new NotFoundException("Il mentore specificato non esiste: " + nome))).toList();
        Utente giudice = repositoryUtenti.findByNomeUtente(nomeGiudice).orElseThrow(() ->
                new NotFoundException("Il utente non esiste: " + nomeGiudice));
        return new HashMap<>(){{
            put(giudice, RuoloStaff.GIUDICE);
            mentori.forEach(mentore -> put(mentore, RuoloStaff.MENTORE));
        }};
    }

    /**
     * Controlla che l'organizzatore esista come utente e lo aggiunge allo staff
     * @param idUtente l'id dell'utente
     * @param hackathon l'hackathon
     */
    private void gestisciOrganizzatore(String idUtente, Hackathon hackathon) {
        Utente organizzatore = repositoryUtenti.findById(idUtente).orElseThrow(() -> new NotFoundException("L' utente "
                + "non esiste: " + idUtente));
        Staff staffOrganizzatore = new Staff(organizzatore, hackathon, RuoloStaff.ORGANIZZATORE);
        hackathon.aggiungiStaff(staffOrganizzatore);
        repositoryStaff.save(staffOrganizzatore);

    }
}
