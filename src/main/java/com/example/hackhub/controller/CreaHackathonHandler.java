package com.example.hackhub.controller;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.eccezioni.ForbiddenException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryStaff;
import com.example.hackhub.repository.RepositoryUtenti;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CreaHackathonHandler {

    private final HackathonBuilder builder;
    private final RepositoryUtenti repositoryUtenti;
    private final RepositoryHackathon repositoryHackathon;
    private final RepositoryStaff repositoryStaff;

    /**
     * Crea un handler che si occupa di gestire tutte le operazioni necessarie per creare un hackathon
     * @param builder il builder per creare l'hackathon
     * @param repositoryUtenti la repository per recuperare gli utenti che saranno organizzatori, mentori e giudici
     * @param repositoryHackathon la repository per salvare l'hackathon creato
     * @param repositoryStaff la repository per salvare i membri dello staff associati all'hackathon
     */
    public CreaHackathonHandler(HackathonBuilder builder, RepositoryUtenti repositoryUtenti, RepositoryHackathon repositoryHackathon, RepositoryStaff repositoryStaff) {
        this.builder = builder;
        this.repositoryUtenti = repositoryUtenti;
        this.repositoryHackathon = repositoryHackathon;
        this.repositoryStaff = repositoryStaff;
    }

    /**
     * Avvia la creazione di un hackathon, verificando che il nome dell'hackathon non sia già esistente. Se tutte le
     * verifiche passano, imposta i dati dell'hackathon usando il builder, imposta l'organizzatore, invia gli inviti a
     * giudice e mentori, e salva l'hackathon nel database.
     * @param idUtente l'ID dell'utente che vuole creare l'hackathon, che sarà l'organizzatore
     * @param nomeHackathon il nome dell'hackathon da creare
     * @param dataInizio la data di inizio dell'hackathon
     * @param dataFine la data di fine dell'hackathon
     * @param luogo il luogo in cui si svolgerà l'hackathon
     * @param premio il premio in denaro per il vincitore dell'hackathon
     * @param teamMin il numero minimo di persone che devono formare un team per partecipare all'hackathon
     * @param teamMax il numero massimo di persone che possono formare un team per partecipare all'hackathon
     * @param maxIscrizioni il numero massimo di team che possono iscriversi all'hackathon
     * @param regolamento il regolamento dell'hackathon, che deve essere una stringa non vuota //TODO come gestiamo il regolamento? è una stringa o un file?
     * @param nomeGiudice il nome dell'utente da invitare come giudice dell'hackathon, che deve esistere nel database
     * @param nomiMentori la lista dei nomi degli utenti da invitare come mentori dell'hackathon, che devono esistere nel database
     * @throws ForbiddenException se esiste già un hackathon con lo stesso nome
     */
    @Transactional
    public void avviaCreazioneHackathon(String idUtente, String nomeHackathon, LocalDate dataInizio, LocalDate dataFine,
                                        String luogo, BigDecimal premio, int teamMin, int teamMax, int maxIscrizioni,
                                        String regolamento, String nomeGiudice, List<String> nomiMentori) {
        if (repositoryHackathon.existsByNome(nomeHackathon)){
            throw new ForbiddenException("Esiste già un hackathon con questo nome");
        }
        builder.impostaNome(nomeHackathon);
        Periodo periodo = new Periodo(dataInizio, dataFine);
        builder.impostaPeriodo(periodo);
        builder.impostaLuogo(luogo);
        builder.impostaPremio(premio);
        builder.impostaTeamMin(teamMin);
        builder.impostaTeamMax(teamMax);
        builder.impostaRegolamento(regolamento);
        builder.impostaMaxIscrizioni(maxIscrizioni);
        Hackathon hackathon = builder.getRisultato();
        gestisciOrganizzatore(idUtente, hackathon);
        gestisciInvitiStaff(hackathon, nomiMentori, nomeGiudice);
        repositoryHackathon.save(hackathon);
    }

    private void gestisciInvitiStaff(Hackathon hackathon, List<String> nomiMentori, String nomeGiudice) {
        ServizioNotifiche servizioNotifiche = new ServizioNotifiche();
        Map<Utente, RuoloStaff> destinatari = gestisciStaff(nomiMentori, nomeGiudice);
        servizioNotifiche.inviaInvitoStaff(hackathon.getNome(), destinatari);
    }

    private Map<Utente, RuoloStaff> gestisciStaff(List<String> nomiMentori, String nomeGiudice) {
        List<Utente> mentori = nomiMentori.stream().map(nome -> repositoryUtenti.findByNome(nome).orElseThrow(() ->
                new NotFoundException("Il mentore specificato non esiste: " + nome))).toList();
        Utente giudice = repositoryUtenti.findByNome(nomeGiudice).orElseThrow(() ->
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
