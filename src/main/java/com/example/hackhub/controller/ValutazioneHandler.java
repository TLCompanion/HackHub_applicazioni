package com.example.hackhub.controller;

import com.example.hackhub.boundary.dto.ValutazioneRequest;
import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.Concluso;
import com.example.hackhub.eccezioni.*;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositorySottomissioni;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ValutazioneHandler {

    private final RepositorySottomissioni repositorySottomissioni;
    private final RepositoryHackathon repositoryHackathon;

    public ValutazioneHandler(RepositorySottomissioni repositorySottomissioni, RepositoryHackathon repositoryHackathon) {
        this.repositorySottomissioni = repositorySottomissioni;
        this.repositoryHackathon = repositoryHackathon;
    }

    /*
    public void avviaInserimentoValutazione(Sottomissione sottomissione)
    non va bene per una REST API. Perché via rete non ti arriva un oggetto dominio già bello
    pronto: ti arriva un ID nell’URL e un JSON con i dati della valutazione.

    @Transactional serve a far eseguire tutto quel metodo come un’unica “unità atomica” sul database: o va tutto a
    buon fine, o se qualcosa esplode a metà, il DB torna com’era prima (rollback). È il modo standard per evitare dati
    incoerenti quando fai più operazioni collegate.
     */
//    @Transactional
//    public void avviaInserimentoValutazione(String idSottomissione, String idGiudice, String giudizio, int punteggio){
//        if (punteggio < 0 || punteggio > 10) {
//            throw new BadRequestException("Valutazione non valida: il punteggio deve essere compreso tra 0 e 10");
//        }
//        // 1) Recupero dati necessari (sottomissione + hackathon)
//        Sottomissione sottomissione = repositorySottomissioni.findById(idSottomissione).orElseThrow(() ->
//                new NotFoundException("Sottomissione non trovata"));
//        Hackathon hackathon = caricaHackathonDellaSottomissioneOrFail(sottomissione);
//        // 2) Validazioni di dominio e permessi
//        // Se verificaValutazioneConsentita lancia RuntimeException/IllegalStateException,
//        // traduciamola in una 409 sensata.
//        try {
//            hackathon.getStato().verificaValutazioneConsentita(hackathon);
//        } catch (RuntimeException ex) {
//            //è importante che usiamo il ResponseStatusException per restituire un codice HTTP specifico in caso
//            // di errore, altrimenti Spring lo tradurrebbe in una 500 generica
//            throw new ConflictException("Valutazione non consentita in questo stato dell'hackathon");
//        }
//        verificaGiudiceAutorizzato(hackathon, idGiudice);
//        // 3) Upsert valutazione (crea se assente, aggiorna se presente)
//        creaOAggiornaValutazione(sottomissione, punteggio, giudizio);
//        // 4) Persistenza sottomissione aggiornata
//        repositorySottomissioni.save(sottomissione);
//        // 5) Se tutto è valutato, conclude l’hackathon
//        concludiHackathonSeTutteValutate(hackathon);
//    }

    @Transactional
    public void avviaInserimentoValutazione(String idHackathon, String idSottomissione, String idGiudice, ValutazioneRequest request){
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
        Hackathon hackathon = repositoryHackathon.findById(idHackathon).orElseThrow(() ->
                new NotFoundException("Hackathon non trovato"));
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

    private void verificaGiudiceAutorizzato(Hackathon hackathon, String idGiudice) {
        boolean autorizzato = hackathon.getStaff().stream()
                .anyMatch(s -> s.getRuolo() == RuoloStaff.GIUDICE && s.getIdUtente().equals(idGiudice));
        if (!autorizzato) {
            throw new ForbiddenException("Utente non autorizzato a valutare questa sottomissione");
        }
    }

    private void creaOAggiornaValutazione(Sottomissione sottomissione, int punteggio, String giudizio) {
        Valutazione valutazione = sottomissione.getValutazione();

        if (valutazione == null) {
            valutazione = new Valutazione(punteggio, giudizio);
            sottomissione.impostaValutazione(valutazione);
        } else {
            valutazione.setVoto(punteggio);
            valutazione.setDescrizione(giudizio);
        }
    }

    private void concludiHackathonSeTutteValutate(Hackathon hackathon) {
        List<Sottomissione> sottomissioni = repositorySottomissioni.findByHackathon(hackathon);
        boolean tutteValutate = sottomissioni.stream().allMatch(Sottomissione::haValutazione);
        if (tutteValutate) {
            hackathon.setStato(Concluso.INSTANCE);
            repositoryHackathon.save(hackathon);
        }
    }

}
