package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.StatoRichiesta;

/**
 * Gestione degli inviti da parte dell'organizzatore allo staff per l'hackathon
 */
public class InvitoStaff extends Richiesta {

    private String idHackathon;
    private RuoloStaff ruoloOfferto;

    /**
     * Crea un'invito da inviare ad un'utente per farlo diventare parte dello Staff di un'hackathon
     * @param idRichiesta l'identificativo della richiesta
     * @param idMittente l'identificativo del mittente
     * @param stato lo stato della richiesta
     * @param idHackathon l'identificativo dell'hackathon
     * @param ruoloOfferto il ruolo offerto dall'organizzatore
     */
    public InvitoStaff(String idRichiesta, String idMittente, StatoRichiesta stato, String idHackathon, RuoloStaff ruoloOfferto) {
        super(idRichiesta, idMittente, stato);
        this.idHackathon = idHackathon;
        this.ruoloOfferto = ruoloOfferto;
    }
}
