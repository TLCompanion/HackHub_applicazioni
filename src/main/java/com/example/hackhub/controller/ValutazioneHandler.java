package com.example.hackhub.controller;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.domain.implementazione.Sottomissione;
import com.example.hackhub.domain.implementazione.Staff;
import com.example.hackhub.domain.implementazione.Valutazione;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositorySottomissioni;
import com.example.hackhub.repository.RepositoryStaff;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValutazioneHandler {

    private final RepositorySottomissioni repositorySottomissioni;
    private final RepositoryHackathon repositoryHackathon;
    private final RepositoryStaff repositoryStaff;

    public ValutazioneHandler(RepositorySottomissioni repositorySottomissioni, RepositoryHackathon repositoryHackathon, RepositoryStaff repositoryStaff) {
        this.repositorySottomissioni = repositorySottomissioni;
        this.repositoryHackathon = repositoryHackathon;
        this.repositoryStaff = repositoryStaff;
    }

    /*
    public void avviaInserimentoValutazione(Sottomissione sottomissione)
    non va bene per una REST API. Perché via rete non ti arriva un oggetto dominio già bello
    pronto: ti arriva un ID nell’URL e un JSON con i dati della valutazione.
     */
    public void avviaInserimentoValutazione(String id, String idGiudice, String giudizio, int punteggio){
        //1. recupera la sottomissione dal repository
        Sottomissione sottomissione = repositorySottomissioni.findById(id).orElseThrow(() -> new RuntimeException("Sottomissione non trovata"));

        //2. controlli che l’hackathon sia in fase di valutazione e non concluso
        // TODO fare in modo che la sottomissione abbia il riferimento all'hackathon a cui appartiene, così da non dover fare un'altra query per recuperarlo
        String hackathonId = sottomissione.getHackathonId();
        Hackathon hackathon = repositoryHackathon.findById(hackathonId).orElseThrow(() -> new RuntimeException("Hackathon non trovato"));
        //TODO inserire le classi stato
        if(hackathon.getStato() != Stato.VALUTAZIONE){
            throw new RuntimeException("Hackathon non in fase di valutazione");
        }

        //Extra: Controllo giudice --> Il JWT ti dice: “questa richiesta arriva da un utente valido (id=123) e il token non è falso”. Fine. Non ti dice automaticamente: “questo utente ha il permesso di valutare questa sottomissione di questo hackathon in questo momento”.
        for (Staff membroStaff : hackathon.getStaff()){
            if (membroStaff.getRuolo().equals(RuoloStaff.GIUDICE)){
                if (!membroStaff.getIdUtente().equals(idGiudice)) {
                    throw new RuntimeException("Utente non autorizzato a valutare questa sottomissione");
                }
            }
        }

        //3. controlli che il punteggio sia 0–10
        if (punteggio < 0 || punteggio > 10){
            throw new RuntimeException("Il punteggio deve essere compreso tra 0 e 10");
        }

        //4. crei o aggiorni la valutazione
        Valutazione valutazione = new Valutazione(punteggio, giudizio);
        sottomissione.impostaValutazione(valutazione);

        //5. salvi
        repositorySottomissioni.save(sottomissione);

        //6. controlli se tutte le sottomissioni dell’hackathon sono valutate → se sì, setti hackathon a “concluso” e salvi
            boolean tutteValutate = true;
            List<Sottomissione> sottomissioni = hackathon.getSottomissioni();
            for (Sottomissione s : sottomissioni){
                if (!s.haValutazione()){
                    tutteValutate = false;
                    break;
                }
            }
            if (tutteValutate){
                hackathon.setStato(Stato.CONCLUSO);
                repositoryHackathon.save(hackathon);
            }
    }
}
