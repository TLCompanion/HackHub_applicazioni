package com.example.hackhub.controller;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.Concluso;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.ForbiddenException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryMembriTeam;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class GestioneCallHandler {

    private final RepositoryMembriTeam repositoryMembriTeam;
    private final RepositoryHackathon repositoryHackathon;

    /**
     * Costruttore per GestioneCallHandler, che riceve in input i repository necessari per gestire le call proposte dai
     * mentori.
     * @param repositoryMembroTeam la repository per recuperare i membri del team e verificare che l'utente che propone
     *                             la call sia un mentore autorizzato
     * @param repositoryHackathon la repository per recuperare l'hackathon a cui è iscritto il team e verificare che
     *                            la call sia proposta prima della fine dell'hackathon
     */
    public GestioneCallHandler(RepositoryMembriTeam repositoryMembroTeam, RepositoryHackathon repositoryHackathon) {
        this.repositoryMembriTeam = repositoryMembroTeam;
        this.repositoryHackathon = repositoryHackathon;
    }

    /**
     * Avvia la proposta di una call da parte di un mentore per un team iscritto ad un hackathon. La call dura mezz'ora
     * e deve essere proposta prima della fine dell'hackathon.
     * @param idUtente l'ID dell'utente che propone la call, che deve essere un mentore autorizzato per l'hackathon
     * @param idHackathon l'ID dell'hackathon a cui è iscritto il team per cui si propone la call
     * @param idTeam l'ID del team per cui si propone la call, che deve essere iscritto all'hackathon
     * @param data la data in cui si propone la call, che deve essere prima della fine dell'hackathon
     * @param ora l'ora in cui si propone la call, che deve essere prima della fine dell'hackathon
     */
    public void avviaPropostaCall(String idUtente, String idHackathon, String idTeam, LocalDate data, LocalTime ora) {
        Hackathon hackathon = repositoryHackathon.findById(idHackathon).orElseThrow( () ->
                new NotFoundException("Hackathon non esistente"));
        verificaMentoreAutorizzato(hackathon, idUtente);
        //Il periodo è fisso e dura mezz'ora
        Periodo periodo = new Periodo(data, ora, data, ora.plusMinutes(30));
        validazione(periodo, hackathon, idTeam);
        ServizioNotifiche servizioNotifiche = new ServizioNotifiche();
        Utente leader = repositoryMembriTeam.findUtenteByRuolo(RuoloTeam.LEADER).orElseThrow(() ->
                new NotFoundException("Leader del team non trovato"));
        servizioNotifiche.inviaPropostaCall(idUtente, hackathon.getNome(), leader, periodo);

    }

    private void validazione(Periodo periodo, Hackathon hackathon, String idTeam) {
        if (hackathon.getStato().equals(Concluso.INSTANCE)) {
            throw new ConflictException("Hackathon concluso, non è possibile proporre una call");
        }
        if(hackathon.getIscrizioni().stream().noneMatch(i -> i.getTeam().getIdTeam().equals(idTeam))) {
            throw new ConflictException("Il team non è iscritto all'hackathon");
        }
        if(periodo.getDataFine().isAfter(hackathon.getPeriodo().getDataFine())) {
            throw new ConflictException("La call non può essere dopo la fine dell'hackathon");
        }
    }

    private void verificaMentoreAutorizzato(Hackathon hackathon, String idMentore) {
        boolean autorizzato = hackathon.getStaff().stream()
                .anyMatch(s -> s.getRuolo() == RuoloStaff.GIUDICE && s.getIdUtente().equals(idMentore));
        if (!autorizzato) {
            throw new ForbiddenException("Utente non autorizzato a valutare questa sottomissione");
        }
    }
}
