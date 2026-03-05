package com.example.hackhub.controller;

import com.example.hackhub.boundary.dto.HackathonRequest;
import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.*;
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
        Hackathon hackathon = builder.getRisultato();
        gestisciOrganizzatore(idUtente, hackathon);
        gestisciInvitiStaff(hackathon, request.nomeMentori(), request.nomeGiudice());
        repositoryHackathon.save(hackathon);
    }

    private void gestisciInvitiStaff(Hackathon hackathon, List<String> nomiMentori, String nomeGiudice) {
        Map<Utente, RuoloStaff> destinatari = gestisciStaff(nomiMentori, nomeGiudice);
        servizioNotifiche.inviaInvitoStaff(hackathon, destinatari);
    }

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

    private void gestisciOrganizzatore(String idUtente, Hackathon hackathon) {
        Utente organizzatore = repositoryUtenti.findById(idUtente).orElseThrow(() -> new NotFoundException("L' utente "
                + "non esiste: " + idUtente));
        Staff staffOrganizzatore = new Staff(organizzatore, hackathon, RuoloStaff.ORGANIZZATORE);
        hackathon.aggiungiStaff(staffOrganizzatore);
        repositoryStaff.save(staffOrganizzatore);

    }
}
