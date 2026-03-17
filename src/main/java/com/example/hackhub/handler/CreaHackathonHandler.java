package com.example.hackhub.handler;

import com.example.hackhub.boundary.dto.HackathonRequest;
import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.servizi.HackathonBuilder;
import com.example.hackhub.servizi.ServizioNotifiche;
import com.example.hackhub.eccezioni.ForbiddenException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
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
    private final ServizioNotifiche servizioNotifiche;

    /**
     * Crea un handler che si occupa di gestire tutte le operazioni necessarie per creare un hackathon
     * @param repositoryUtenti la repository per recuperare gli utenti che saranno organizzatori, mentori e giudici
     * @param repositoryHackathon la repository per salvare l'hackathon creato
     * @param servizioNotifiche il servizio per inviare le notifiche agli utenti invitati come giudici e mentori
     */
    public CreaHackathonHandler(RepositoryUtenti repositoryUtenti, RepositoryHackathon
            repositoryHackathon, ServizioNotifiche servizioNotifiche) {
        this.repositoryUtenti = repositoryUtenti;
        this.repositoryHackathon = repositoryHackathon;
        this.servizioNotifiche = servizioNotifiche;
    }

    /**
     * Avvia la creazione di un hackathon, verificando che il nome dell'hackathon non sia già esistente. Se tutte le
     * verifiche passano, imposta i dati dell'hackathon usando il builder, imposta l'organizzatore, invia gli inviti a
     * giudice e mentori, e salva l'hackathon nel database.
     * @param request la richiesta di creazione
     * @param nomeUtente il nome utente dell'organizzatore che sta creando l'hackathon
     */
    @Transactional
    public void avviaCreazioneHackathon(HackathonRequest request, String nomeUtente) {
        if (repositoryHackathon.existsByNome(request.nome())){
            throw new ForbiddenException("Esiste già un hackathon con questo nome");
        }
        if (request.nomeGiudice().equals(nomeUtente) || request.nomeMentori().contains(nomeUtente)) {
            throw new ForbiddenException("L'organizzatore non può essere anche giudice o mentore");
        }
        HackathonBuilder builder = new HackathonBuilder();
        builder.reset();
        buildSteps(builder, request);
        Hackathon hackathon = builder.getRisultato();
        repositoryHackathon.save(hackathon);
        gestisciOrganizzatore(nomeUtente, hackathon);
        gestisciInvitiStaff(hackathon, request.nomeMentori(), request.nomeGiudice());
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
        String nomeOrganizzatore = hackathon.getStaff().stream().filter(s -> s.getRuolo().equals(
                RuoloStaff.ORGANIZZATORE)).findFirst().orElseThrow(() ->
                new NotFoundException("L'organizzatore non è stato trovato")).getUtente().getNomeUtente();
        for (Utente d : utentiDestinatari)
            servizioNotifiche.creaInvitoStaff(nomeOrganizzatore, d, hackathon, destinatari.get(d));
    }

    /**
     * Controlla che i nomi degli utenti legati allo staff siano presenti nel sistema
     * @param nomiMentori i nomi dei mentori
     * @param nomeGiudice il nome del giudice
     * @return una nuova HashMap che associa l'utente esistente al suo ruolo
     */
    private Map<Utente, RuoloStaff> gestisciStaff(List<String> nomiMentori, String nomeGiudice) {
        List<Utente> mentori = nomiMentori.stream().map(nome -> repositoryUtenti.findByNomeUtente(nome).orElseThrow(() ->
                new NotFoundException("L'utente specificato non esiste: " + nome))).toList();
        Utente giudice = repositoryUtenti.findByNomeUtente(nomeGiudice).orElseThrow(() ->
                new NotFoundException("L'utente specificato non esiste: " + nomeGiudice));
        if (mentori.contains(giudice)) {
            throw new ForbiddenException("Un utente non può essere sia giudice che mentore");
        }
        return new HashMap<>(){{
            put(giudice, RuoloStaff.GIUDICE);
            mentori.forEach(mentore -> put(mentore, RuoloStaff.MENTORE));
        }};
    }

    /**
     * Controlla che l'organizzatore esista come utente e lo aggiunge allo staff
     * @param nomeUtente il nome utente dell'organizzatore
     * @param hackathon l'hackathon
     */
    private void gestisciOrganizzatore(String nomeUtente, Hackathon hackathon) {
        Utente organizzatore = repositoryUtenti.findByNomeUtente(nomeUtente).orElseThrow(() ->
                new NotFoundException("L' utente non esiste: " + nomeUtente));
        Staff staffOrganizzatore = new Staff(organizzatore, RuoloStaff.ORGANIZZATORE);
        hackathon.aggiungiStaff(staffOrganizzatore);
        repositoryHackathon.save(hackathon);
    }
}
