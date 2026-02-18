package com.example.hackhub.controller;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.Concluso;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositorySottomissioni;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    @Transactional
    public void avviaInserimentoValutazione(String idSottomissione, String idGiudice, String giudizio, int punteggio){
        validaInput(idSottomissione, idGiudice, giudizio, punteggio);
        // 1) Recupero dati necessari (sottomissione + hackathon)
        Sottomissione sottomissione = repositorySottomissioni.findById(idSottomissione).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sottomissione non trovata"));
        Hackathon hackathon = caricaHackathonDellaSottomissioneOrFail(sottomissione);
        // 2) Validazioni di dominio e permessi
        // Se verificaValutazioneConsentita lancia RuntimeException/IllegalStateException,
        // traduciamola in una 409 sensata.
        try {
            hackathon.getStato().verificaValutazioneConsentita(hackathon);
        } catch (RuntimeException ex) {
            //è importante che usiamo il ResponseStatusException per restituire un codice HTTP specifico in caso
            // di errore, altrimenti Spring lo tradurrebbe in una 500 generica
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Valutazione non consentita nello stato attuale dell'hackathon",
                    ex
            );
        }
        verificaGiudiceAutorizzato(hackathon, idGiudice);
        if (punteggio < 0 || punteggio > 10) {
            throw new RuntimeException("Il punteggio deve essere compreso tra 0 e 10");
        }
        // 3) Upsert valutazione (crea se assente, aggiorna se presente)
        creaOAggiornaValutazione(sottomissione, punteggio, giudizio);
        // 4) Persistenza sottomissione aggiornata
        repositorySottomissioni.save(sottomissione);
        // 5) Se tutto è valutato, conclude l’hackathon
        concludiHackathonSeTutteValutate(hackathon);
    }

    private void validaInput(String idSottomissione, String idGiudice, String giudizio, int punteggio) {
        if (idSottomissione == null || idSottomissione.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idSottomissione mancante");
        }
        if (idGiudice == null || idGiudice.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idGiudice mancante");
        }
        if (giudizio == null || giudizio.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "giudizio mancante");
        }
        if (punteggio < 0 || punteggio > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il punteggio deve essere compreso tra 0 e 10");
        }
    }

    private Hackathon caricaHackathonDellaSottomissioneOrFail(Sottomissione sottomissione) {
        // Se sottomissione.getHackathon() fosse null è incoerenza -> 500
        if (sottomissione.getHackathon() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Sottomissione senza hackathon associato (dati incoerenti)");
        }
        String hackathonId = sottomissione.getHackathon().getIdHackathon();
        if (hackathonId == null || hackathonId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Sottomissione con idHackathon mancante (dati incoerenti)");
        }
        return repositoryHackathon.findById(hackathonId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Hackathon associato alla sottomissione non trovato (dati incoerenti)"
                ));
    }

    private void verificaGiudiceAutorizzato(Hackathon hackathon, String idGiudice) {
        boolean autorizzato = hackathon.getStaff().stream()
                .anyMatch(s -> s.getRuolo() == RuoloStaff.GIUDICE && s.getIdUtente().equals(idGiudice));
        if (!autorizzato) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Utente non autorizzato a valutare questo hackathon");
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
        boolean tutteValutate = sottomissioni.stream().allMatch(Sottomissione::haValutazione);;
        if (tutteValutate) {
            hackathon.setStato(Concluso.INSTANCE);
            repositoryHackathon.save(hackathon);
        }
    }

}
