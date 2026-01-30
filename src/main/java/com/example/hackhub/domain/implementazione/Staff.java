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

    //metodi getter

    public RuoloStaff getRuolo() {return ruolo;}

    public String getIdHackathon() {return idHackathon;}

    public String getIdUtente() {return idUtente;}
}
