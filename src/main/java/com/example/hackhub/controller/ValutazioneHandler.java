package com.example.hackhub.controller;

import com.example.hackhub.boundary.dto.ValutazioneRequest;
import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.Concluso;
import com.example.hackhub.eccezioni.*;
import com.example.hackhub.repository.*;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ValutazioneHandler{

    private final RepositorySottomissioni repositorySottomissioni;
    private final RepositoryHackathon repositoryHackathon;
    private final RepositoryStaff repositoryStaff;
    private final ServizioNotifiche servizioNotifiche;
    private final RepositoryIscrizioniTeam repositoryIscrizioniTeam;
    private final RepositoryValutazioni repositoryValutazioni;

    /**
     * Crea una nuova istanza di un handler per la valutazione delle sottomissioni
     * @param repositorySottomissioni la repository delle sottomissioni
     * @param repositoryHackathon la repository degli hackathon
     * @param repositoryStaff la repository dello staff
     * @param servizioNotifiche il servizio per le notifiche
     */
    public ValutazioneHandler(RepositorySottomissioni repositorySottomissioni, RepositoryHackathon repositoryHackathon,
                              RepositoryStaff repositoryStaff, ServizioNotifiche servizioNotifiche, RepositoryIscrizioniTeam repositoryIscrizioniTeam, RepositoryValutazioni repositoryValutazioni) {
        this.repositorySottomissioni = repositorySottomissioni;
        this.repositoryHackathon = repositoryHackathon;
        this.repositoryStaff = repositoryStaff;
        this.servizioNotifiche = servizioNotifiche;
        this.repositoryIscrizioniTeam = repositoryIscrizioniTeam;
        this.repositoryValutazioni = repositoryValutazioni;
    }

    /**
     * Avvia l'inserimento di una nuova valutazione per una sottomissione
     * @param idSottomissione l'id della sottomissione da valutare
     * @param idGiudice l'id del giudice
     * @param request la valutazione con i suoi componenti
     */
    @Transactional
    public void avviaInserimentoValutazione(String idSottomissione, String idGiudice, ValutazioneRequest request){
        //Giagi questo non serve perchè c'è già il controllo sul DTO, e se il punteggio è fuori range, Spring restituirà
        // automaticamente un 400 Bad Request con un messaggio di errore dettagliato. Quindi non è necessario fare un
        // controllo manuale qui, a meno che tu non voglia personalizzare ulteriormente il messaggio di errore o gestire
        // il caso in modo specifico.
        /*if (request.punteggio() < 0 || request.punteggio() > 10) {
            throw new BadRequestException("Valutazione non valida: il punteggio deve essere compreso tra 0 e 10");
        }
         */
        // 1) Recupero dati necessari (sottomissione + hackathon)
        Sottomissione sottomissione = repositorySottomissioni.findById(idSottomissione).orElseThrow(() ->
                new NotFoundException("Sottomissione non trovata"));
        Hackathon hackathon = repositoryStaff.findById(idGiudice).orElseThrow( () ->
                new NotFoundException("Giudice non trovato")).getHackathon();
        // 2) Validazioni di dominio e permessi
        // Se verificaValutazioneConsentita lancia RuntimeException/IllegalStateException,
        // traduciamola in una 409 sensata.
        try {
            hackathon.getStato().verificaValutazioneConsentita(hackathon);
        } catch (RuntimeException ex) {
            //è importante che usiamo il ResponseStatusException per restituire un codice HTTP specifico in caso
            // di errore, altrimenti Spring lo tradurrebbe in una 500 generica
            throw new ConflictException("Valutazione non consentita in questo stato dell'hackathon");
        }
        verificaGiudiceAutorizzato(hackathon, idGiudice);
        // 3) Upsert valutazione (crea se assente, aggiorna se presente)
        creaOAggiornaValutazione(sottomissione, request.punteggio(), request.giudizio());
        // 4) Persistenza sottomissione aggiornata
        repositorySottomissioni.save(sottomissione);
        // 5) Se tutto è valutato, conclude l’hackathon
        concludiHackathonSeTutteValutate(hackathon);
    }

    /**
     * Verifica che l'utente che vuole valutare le sottomissioni sia il giudice dell'hackathon
     * @param hackathon l'hackathon
     * @param idGiudice l'id del giudice
     */
    private void verificaGiudiceAutorizzato(Hackathon hackathon, String idGiudice) {
        boolean autorizzato = hackathon.getStaff().stream()
                .anyMatch(s -> s.getRuolo() == RuoloStaff.GIUDICE && s.getIdUtente().equals(idGiudice));
        if (!autorizzato) {
            throw new ForbiddenException("Utente non autorizzato a valutare questa sottomissione");
        }
    }

    /**
     * Crea o aggiorna una valutazione per una sottomissione
     * @param sottomissione la sottomissione da valutare
     * @param punteggio il punteggio associato
     * @param giudizio il giudizio associato
     */
    private void creaOAggiornaValutazione(Sottomissione sottomissione, int punteggio, String giudizio) {
        Valutazione valutazione = sottomissione.getValutazione();

        if (valutazione == null) {
            valutazione = new Valutazione(punteggio, giudizio);
            sottomissione.impostaValutazione(valutazione);
            repositoryValutazioni.save(valutazione);
        } else {
            valutazione.setVoto(punteggio);
            valutazione.setDescrizione(giudizio);
            repositoryValutazioni.save(valutazione);
        }
    }

    /**
     * Conclude l'hackathon se tutte le sottomissioni sono state valutate
     * @param hackathon l'hackathon
     */
    private void concludiHackathonSeTutteValutate(Hackathon hackathon) {
        List<Sottomissione> sottomissioni = repositoryIscrizioniTeam.findAllByHackathon(hackathon).stream().
                map(IscrizioneTeam::getSottomissione).filter(Objects::nonNull).toList();
        boolean tutteValutate = sottomissioni.stream().allMatch(Sottomissione::haValutazione);
        if (tutteValutate) {
            hackathon.conludiHackathon();
            repositoryHackathon.save(hackathon);
            String messaggio = "L'hackathon è stato concluso, valutazioni terminate";
            List<Utente> utentiDestinatari = hackathon.getIscrizioni().stream()
                    .filter(i -> i.getHackathon().equals(hackathon))
                    .map(IscrizioneTeam::getTeam)
                    .flatMap(team -> team.getMembri().stream())
                    .map(MembroTeam::getUtente)
                    .toList();
            servizioNotifiche.creaNotifica(utentiDestinatari, TipoNotifica.VALUTAZIONE_CONCLUSA, messaggio);
        }
    }

}
