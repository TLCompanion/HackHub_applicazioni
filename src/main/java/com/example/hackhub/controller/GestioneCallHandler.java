package com.example.hackhub.controller;

import com.example.hackhub.boundary.dto.PropostaCallRequest;
import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.Concluso;
import com.example.hackhub.servizi.ServizioNotifiche;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.ForbiddenException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryMembriTeam;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class GestioneCallHandler {

    private final RepositoryMembriTeam repositoryMembriTeam;
    private final RepositoryHackathon repositoryHackathon;
    private final ServizioNotifiche servizioNotifiche;

    /**
     * Costruttore per GestioneCallHandler, che riceve in input i repository necessari per gestire le call proposte dai
     * mentori.
     * @param repositoryMembroTeam la repository per recuperare i membri del team e verificare che l'utente che propone
     *                             la call sia un mentore autorizzato
     * @param repositoryHackathon la repository per recuperare l'hackathon a cui è iscritto il team e verificare che
     *                            la call sia proposta prima della fine dell'hackathon
     * @param servizioNotifiche il servizio per inviare le notifiche al leader del team quando viene proposta una call
     */
    public GestioneCallHandler(RepositoryMembriTeam repositoryMembroTeam, RepositoryHackathon repositoryHackathon, ServizioNotifiche servizioNotifiche) {
        this.repositoryMembriTeam = repositoryMembroTeam;
        this.repositoryHackathon = repositoryHackathon;
        this.servizioNotifiche = servizioNotifiche;
    }

    @Transactional
    public void avviaPropostaCall(String idUtente, PropostaCallRequest request) {
        Hackathon hackathon = repositoryHackathon.findById(request.idHackathon()).orElseThrow( () ->
                new NotFoundException("Hackathon non esistente"));
        verificaMentoreAutorizzato(hackathon, idUtente);
        //Il periodo è fisso e dura mezz'ora
        Periodo periodo = new Periodo(request.data(), request.ora(), request.data(), request.ora().plusMinutes(30));
        validazione(periodo, hackathon, request.idTeam());
        Utente leader = repositoryMembriTeam.findMembroTeamByRuolo(RuoloTeam.LEADER).orElseThrow(() ->
                new NotFoundException("Leader del team non trovato")).getUtente();
       // servizioNotifiche.inviaPropostaCall(idUtente, hackathon, leader, periodo);

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
