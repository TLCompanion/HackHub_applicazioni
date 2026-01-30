package com.example.hackhub.domain.implementazione;

import java.math.BigDecimal;

public class Validatore {

    private Validatore instance;

    /**
     * Costruzione di un'entità di validatore
     */
    public Validatore(){
    }

    /**
     * Se l'istanza è nulla ne creo una nuuova
     * @return l'istanza creata se è null o quella precedentemente esistente se non è nulla
     */
    public Validatore getInstance() {
        if (instance == null) {
            instance = new Validatore();
        }
        return instance;
    }

    public boolean verificaNomeHackathon(String nome){
        //TODO IMPLEMENTARE
        return false;
    }

    public boolean verificaPremio(BigDecimal premio){
        //TODO IMPLEMENTARE
        return false;
    }

    public boolean verificaTeamMax(int teamMax){
        //TODO IMPLEMENTARE
        return false;
    }

    public boolean verificaTeamMin(int teamMin){
        //TODO IMPLEMENTARE
        return false;
    }

    public boolean verificaNomeTeam(String nome){
        //TODO IMPLEMENTARE
        return false;
    }
}
