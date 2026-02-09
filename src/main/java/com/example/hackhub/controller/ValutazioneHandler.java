package com.example.hackhub.controller;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.*;
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
    @Transactional
    public void avviaInserimentoValutazione(String idSottomissione, String idGiudice, String giudizio, int punteggio){
        // 1) Recupero dati necessari (sottomissione + hackathon)
        Sottomissione sottomissione = repositorySottomissioni.findById(idSottomissione).orElseThrow(() -> new RuntimeException("Sottomissione non trovata"));
        Hackathon hackathon = caricaHackathonDellaSottomissioneOrFail(sottomissione);
        // 2) Validazioni di dominio e permessi
        hackathon.getStato().verificaValutazioneConsentita(hackathon);
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

    // TODO stabilire se la sottomissione deve avere un riferimento all'hackathon o se dobbiamo cercare l'hackathon a
    //  partire dalla sottomissione
    private Hackathon caricaHackathonDellaSottomissioneOrFail(Sottomissione sottomissione) {
        String hackathonId = sottomissione.getHackathon().getIdHackathon();
        return repositoryHackathon.findById(hackathonId)
                .orElseThrow(() -> new RuntimeException("Hackathon non trovato"));
    }

    private void verificaGiudiceAutorizzato(Hackathon hackathon, String idGiudice) {
        boolean autorizzato = hackathon.getStaff().stream()
                .anyMatch(s -> s.getRuolo() == RuoloStaff.GIUDICE && s.getIdUtente().equals(idGiudice));
        if (!autorizzato) {
            throw new RuntimeException("Utente non autorizzato a valutare questo hackathon");
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
        boolean tutteValutate = true;
        List<Sottomissione> sottomissioni = hackathon.getSottomissioni();
        for (Sottomissione s : sottomissioni) {
            if (!s.haValutazione()) {
                tutteValutate = false;
                break;
            }
        }
        if (tutteValutate) {
            hackathon.setStato(Concluso.INSTANCE);
            repositoryHackathon.save(hackathon);
        }
    }

}
