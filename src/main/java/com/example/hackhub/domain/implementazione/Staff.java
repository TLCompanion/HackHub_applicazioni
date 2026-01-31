package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloStaff;

/**
 * Classe che gestisce un singolo membro dello staff per un'hackathon e lo associa all'hackathon
 * in cui lavora
 */
public class Staff {

    private String idUtente;
    private String idHackathon;
    private RuoloStaff ruolo;

    /**
     * Creazione di un membro dello staff
     * @param idUtente l'identificativo dell'utente
     * @param idHackathon l'identificativo dell'hackathon associato
     * @param ruolo il ruolo ricoperto
     */
    public Staff(String idUtente, String idHackathon, RuoloStaff ruolo) {
        this.idUtente = idUtente;
        this.idHackathon = idHackathon;
        this.ruolo = ruolo;
    }



    //metodi getter

    public RuoloStaff getRuolo() {return ruolo;}

    public String getIdHackathon() {return idHackathon;}

    public String getIdUtente() {return idUtente;}
}
