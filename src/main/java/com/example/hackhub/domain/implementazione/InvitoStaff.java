package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.StatoRichiesta;
import jakarta.persistence.Entity;

/**
 * Gestione degli inviti da parte dell'organizzatore allo staff per l'hackathon
 */
public class InvitoStaff<T> extends Richiesta {

    //private String idHackathon
    private Hackathon hackathon;
    private RuoloStaff ruoloOfferto;

    /**
     * Crea un'invito da inviare ad un'utente per farlo diventare parte dello Staff di un'hackathon
     * @param idRichiesta l'identificativo della richiesta
     * @param nomeMittente il mittente dell'invito
     * @param hackathon l'hackathon a cui è associato l'invito
     * @param ruoloOfferto il ruolo offerto dall'organizzatore
     */
    public InvitoStaff(String idRichiesta, String nomeMittente, Hackathon hackathon, RuoloStaff ruoloOfferto) {
        super(nomeMittente);
        this.hackathon = hackathon;
        this.ruoloOfferto = ruoloOfferto;
    }
}
